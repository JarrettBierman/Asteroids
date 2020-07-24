import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

//Jarrett Bierman
//The Class where I play my Asteroids Game
//Sep 5, 2017

public class AstRunner extends JPanel implements ActionListener, KeyListener
{
   private static final int PREF_W = 1200;
   private static final int PREF_H = 750;
   private static Ship ship;
   private static ArrayList<Asteroid> ast;
   private static ArrayList<Bullet> b;
   private static int c1, cDeath, score, time, spawnRate, delay;
   private static boolean easyMode, midMode, hardMode, bassDrop, isShooting, paused, chilled;
   private int highScore;
   private String highScoreFile;
   private Point titlePoint;
   private Image bg, pausedScreen;
   private Rectangle bounds;
   private Timer timer;
   private Font font1, font2, font3, font4, font5;
      
   /**
    * Oh Man look it's my game I'm so cool and dope!
    * @author Jarrett Bierman
    *
    */
   public AstRunner()
   {
      this.addKeyListener(this);
      setFocusable(true);
      requestFocus();
      
      titlePoint = new Point(1500, 600); //The title screen at the beginning
      
      bounds = new Rectangle(0,0,PREF_W, PREF_H);
      
      bassDrop = false;
      
      JukeBox.init();
      JukeBox.load("musicPart1.wav", "part1");
      JukeBox.load("musicPart2.wav", "part2");
      JukeBox.load("deathSound.wav", "death");
      JukeBox.load("deathSound.wav", "astDeath");
      JukeBox.load("shootSound2.wav", "shoot");
      JukeBox.load("ChillMusic1.wav", "chillMusic");
      
      JukeBox.setVolume("part1", -2);
      JukeBox.setVolume("part2", -2);
      JukeBox.setVolume("shoot", -3);
      JukeBox.setVolume("astDeath", -6);
      JukeBox.setVolume("death", 6); 
      JukeBox.setVolume("chillMusic", 4); 
     
      highScoreFile = "/Users/" + System.getProperty("user.name") + "/Documents/AsteroidHighScore.txt";
      
      try
      {bg = new ImageIcon(this.getClass().getResource("bg5.gif")).getImage();}
      catch (NullPointerException e){e.printStackTrace();} 
      
      try
      {pausedScreen = new ImageIcon(this.getClass().getResource("PausedScreen2.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      
      
      midMode = true;
      chilled = false;
      
      //Create and Implement the Font on the System
      InputStream is = AstRunner.class.getResourceAsStream("game_over.ttf");
      Font font = null;
      try
      {
         font = Font.createFont(Font.TRUETYPE_FONT, is);
      }catch (FontFormatException | IOException e)  {e.printStackTrace();}
      
      font1 = font.deriveFont(90f);
      font2 = font.deriveFont(300f);
      font3 = font.deriveFont(275f);
      font4 = font.deriveFont(150f);
      font5 = font.deriveFont(30f);
      
      
      
      restart();
      
      this.setBackground(Color.BLACK); 
      timer = new Timer(10, this);
      timer.start();     
   }
   
   public static void restart()
   {
      paused = false;
      bassDrop = false;
      
      if(!chilled)
      {
         JukeBox.stop("part2");
         JukeBox.play("part1");
         JukeBox.stop("chillMusic");
      }
      else if(!JukeBox.isPlaying("chillMusic"))
         JukeBox.loop("chillMusic");
         
      
      ship = new Ship(PREF_W/2, PREF_H/2); 
      ast = new ArrayList<Asteroid>();
      b = new ArrayList<Bullet>();
      c1 = 0;
      time = 0;
      cDeath = 0;
      score = 0;
   }
   
   
   public void addAst(int amount)
   {
      boolean rand = false;
      int r;
      for(int i = 0; i < amount; i++)
      {
         r = (int)(Math.random() * 4);
         rand = !rand;
         int size = (int)(Math.random() * 100) + 150;
         double dx, dy;
         int x, y;
         x = y = 0;
         dx = dy = 0;
         double speed = 2;
         if(r == 0)
         {
            dx =  Math.random() * speed;
            dy = Math.random() * speed*2 - speed;
            x = 0 - size;
            y = (int)Math.random()*PREF_H;
         }
         
         if(r == 1)
         {
            dx =  Math.random() * -speed;
            dy = Math.random() * speed*2 - speed;
            x = PREF_W + size;
            y = (int)Math.random()*PREF_H;
         }
         
         if(r == 2)
         {
            dx =  Math.random() * speed*2 - speed;
            dy = Math.random() * speed;
            x = (int) Math.random() * PREF_W;

            y = 0 - size;
         }
         
         if(r == 3)
         {
            dx =  Math.random() * speed*2 - speed;
            dy = Math.random() * -speed;
            x = (int) Math.random() * PREF_W;
            y = PREF_H + size;
         }
         ast.add(new Asteroid(x, y, dx, dy, size, size, 3));
         
      }
  
   }
   
   public void addAst(int x, int y, double s, int stage, int amount)
   {
      for(int i = 0; i < amount; i++)
      {
         int size = 0;
         double dx = Math.random() * stage - (stage / 2.5);
         double dy = Math.random() * stage - (stage / 2.5);
         
         if(stage == 3)         size = (int)(Math.random() * 50) + 50;
         else if(stage == 2)    size = (int)(Math.random() * 15) + 35;

         ast.add(new Asteroid(x + (int)(s/2), y + (int)(s/2), dx, dy, size, size, stage - 1));
      }
   }
   
   public void shoot()
   {
      if(delay > 0)
         delay--;
      
         if(isShooting && delay == 0)
         {
            b.add(new Bullet(ship.getTop().x, ship.getTop().y, ship.getDegree()));
            JukeBox.play("shoot");
            delay += 30;
         }
   }
   
   public void dontLetTheBulletsDissapear()
   {
      for(Bullet bs : b)
      {
         if(paused)
            bs.getTimer().stop();
         else
            bs.getTimer().start();
      }
   }
   
   public void update()
   {  
      //Spawning Asteroids
      if(easyMode)
         spawnRate = 800;
      else if(midMode)
         spawnRate = 650;
      else if(hardMode)
         spawnRate = 350;
      
      c1++;
      if(c1 % spawnRate == 1 && ast.size() <= 65 && !ship.isDead())
         addAst(2);
      
      if(c1 % 100 == 0 && !ship.isDead())
         time++;
      
      //Killing Asteroids
      for(int i = 0; i < ast.size(); i++)
      {
         for(int j = b.size() - 1; j >= 0; j--)
         {
            if(ast.get(i).getBounds().contains(b.get(j).getX(), b.get(j).getY()))
            {
               b.remove(j);
               if(ast.get(i).getStage() > 1)
                  addAst((int)ast.get(i).getX(), (int)ast.get(i).getY(),ast.get(i).getWidth(), ast.get(i).getStage(), ast.get(i).getStage());
               ast.remove(i);
               JukeBox.play("astDeath");
               score += 10;
               break;
            }
         }
      }
      
      
      //Removing Bullets if they don't hit anything
      for(int i = 0; i < b.size(); i++)
      {
         if(b.get(i).isTimeUp())
            b.remove(i);
      }
     
      //The Ship dying in the bounds
      for(int i = 0; i < ast.size(); i++)
         if(ship.intersectsAst(ast.get(i)))
            doDeath();
   }
   
   public void doDeath()
   {
      if(!ship.isDead())
         JukeBox.play("death");
      ship.setDead(true);
      readHighScore();
      setScores();
   }
 
   public void paintComponent(Graphics g)//////////DRAW METHOD
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
      
      
/////////////////////////////////////////////////////////////      
   //Drawing the Background
      g2.setColor(new Color(128,189,158));   
      if(bassDrop)
         g2.drawImage(bg, 0, 0, PREF_W, PREF_H, this);  
      else if(chilled)
         g2.fillRect(0, 0, PREF_W, PREF_H);
         
   //Drawing the moving title screen
      g2.setFont(font2);
      g2.setColor(Color.GRAY);
      g2.drawString("THIS IS MORE THAN JUST ASTEROIDS                              GET READY", titlePoint.x, titlePoint.y);
      if(titlePoint.x > -5000)
      titlePoint.x -= 8;
      
   //Drawing the small credits thing
      g2.setFont(font5);
      g2.setColor(Color.ORANGE);
      g2.drawString("A project developed by Jarrett Bierman", 10, PREF_H - 10);
      
   //Drawing Asteroids
      for(int i = 0; i < ast.size(); i++)
      {
         if(bassDrop)
            ast.get(i).drawAstPic(g2);
         else if(chilled)
            ast.get(i).drawAstChill(g2);
         else
            ast.get(i).drawAst(g2);     
      }
      
   //Rotate the panel back after Asteroids rotate it
      g2.setTransform(new AffineTransform());
      
   //Drawing Bullets
      for(int i = 0; i < b.size(); i++)
      {
         if(bassDrop)
            b.get(i).drawBulletColor(g2);
         else if(chilled)
            b.get(i).drawBulletChill(g2);
         else
            b.get(i).drawBulletWhite(g2);
      }
      
   //Drawing the Ship
      if(ship.isDead())
      {
        if(cDeath < 100)
           ship.drawExplsion(g2);
      }
      else
      {
         if(bassDrop)
            ship.drawColor(g2);
         else if(chilled)
            ship.drawChill(g2);
         else
            ship.drawWhite(g2);
      }
      
      
   //Drawing the Score, Time, Difficulty, and Ast Count
//      g2.setFont(new Font(s, Font.PLAIN, 30));
      g2.setFont(font1);
      if(chilled)
         g2.setColor(Color.BLACK);
      else
         g2.setColor(Color.MAGENTA);
      g2.drawString("Score: " + score, 50, 50);
      g2.drawString(time + " seconds", 875, 50);
      g2.drawString("Asteroid Count: " + ast.size(), 50, 80);
      String mode = "";
      if(easyMode)  mode = "EASY";
      else if(midMode)  mode = "MEDIUM";
      else if(hardMode)  mode = "HARD";
      g2.drawString("Difficulty: " + mode, 875, 80);
   
   //Drawing the Death Screen
      if(ship.isDead())
      {
         g2.setColor(new Color(100,100,125,150));
         g2.fillRect(0, 0, PREF_W, PREF_H);
         g2.setColor(Color.RED);
         g2.setFont(font3);
         g2.drawString("YOU HAVE EXPLODED", 10, 300);
         g2.setFont(font4);
         g2.drawString("You scored " + score + " points in " + time + " seconds", 150, 400);
         g2.drawString("Your High Score is " + highScore, 250, 475);
         g2.drawString("Press ENTER to RESTART", 250, 550);
      }
      
   //Drawing the Paused Screen
      if(paused)
         g2.drawImage(pausedScreen, 0, 0, PREF_W, PREF_H, null);
   }
   
   public void changeMusic()
   {
      if(!JukeBox.isPlaying("part1") && !bassDrop && !chilled)
         bassDrop = true;
      
      if(bassDrop && !JukeBox.isPlaying("part2") && !chilled)
         JukeBox.loop("part2");
      
      if(chilled)
      {
         JukeBox.stop("part1");
         JukeBox.stop("part2");
      }
   }
  
   
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }
   
   public static void addMenu(JFrame frame)
   {
      MenuBar ret = new MenuBar();
      
      
      Menu musicOptions = new Menu("Music");
            MenuItem mute = new MenuItem("Mute Music");
            MenuItem unMute = new MenuItem("UnMute Music");
            unMute.setEnabled(false);
            
            mute.addActionListener(new ActionListener(){
      
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  JukeBox.setVolume("part1", -Float.MAX_VALUE);   
                  JukeBox.setVolume("part2", -Float.MAX_VALUE);
                  JukeBox.setVolume("chillMusic", -Float.MAX_VALUE);
                  mute.setEnabled(false);
                  unMute.setEnabled(true);
               }
               });
            
            unMute.addActionListener(new ActionListener(){
      
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  JukeBox.setVolume("part1", 0);   
                  JukeBox.setVolume("part2", 0);
                  mute.setEnabled(true);
                  unMute.setEnabled(false);
               }  
            });
            musicOptions.add(mute);
            musicOptions.add(unMute);
            
      Menu difficulty = new Menu("Select Difficulty");
            MenuItem easy = new MenuItem("Easy");
            MenuItem mid = new MenuItem("Medium");
            mid.setEnabled(false);
            MenuItem hard = new MenuItem("Hard");
            
            easy.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  easyMode = true;
                  midMode = false;
                  hardMode = false;
                  easy.setEnabled(false);
                  mid.setEnabled(true);
                  hard.setEnabled(true);
                  restart();
               }  
            });
            
            mid.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  easyMode = false;
                  midMode = true;
                  hardMode = false;
                  easy.setEnabled(true);
                  mid.setEnabled(false);
                  hard.setEnabled(true);
                  restart();
               }  
            });
            
            hard.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  easyMode = false;
                  midMode = false;
                  hardMode = true;
                  easy.setEnabled(true);
                  mid.setEnabled(true);
                  hard.setEnabled(false);
                  restart();
               }  
            });
            difficulty.add(easy);
            difficulty.add(mid);
            difficulty.add(hard);
      Menu chillMode = new Menu("Chill Mode");
            MenuItem enableChill = new MenuItem("Enable Chill Mode");
            MenuItem disableChill = new MenuItem("Disable Chill Mode");
            disableChill.setEnabled(false);
            
            enableChill.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  chilled = true;
                  enableChill.setEnabled(false);
                  disableChill.setEnabled(true);
                  restart();
               } 
            });
            
            disableChill.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  chilled = false;
                  enableChill.setEnabled(true);
                  disableChill.setEnabled(false);
                  restart();
               }  
            });
            
            chillMode.add(enableChill);
            chillMode.add(disableChill);
      
      
      ret.add(musicOptions);
      ret.add(difficulty);
      ret.add(chillMode);
      frame.setMenuBar(ret);
      //ADD MENUITEM OR MORE MENUS TO THE MENU
   }
   
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable(){         
         public void run(){
            createAndShowGUI();
         }        
      });
   }
   
   private static void createAndShowGUI()
   {
      AstRunner gamePanel = new AstRunner();
      
      JFrame frame = new JFrame("Astroids v1.0.1");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(gamePanel);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setVisible(true);
      addMenu(frame);
   }

   @Override
   public void keyTyped(KeyEvent e)
   {
   }

   @Override
   public void keyPressed(KeyEvent e)
   {
      int key = e.getKeyCode();
      
      if(key == KeyEvent.VK_RIGHT)
         ship.setTurnCW(true);
      if(key == KeyEvent.VK_LEFT)
         ship.setTurnCCW(true);
      if(key == KeyEvent.VK_UP)
         ship.setUp(true);
      if(key == KeyEvent.VK_SPACE)
         isShooting = true;
      if(key == KeyEvent.VK_ENTER && ship.isDead())
         restart();
      if((key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_P) && !ship.isDead())
         paused = !paused;
   }

   @Override
   public void keyReleased(KeyEvent e)
   {
      int key = e.getKeyCode();
      
      if(key == KeyEvent.VK_RIGHT)
         ship.setTurnCW(false);
      if(key == KeyEvent.VK_LEFT)
         ship.setTurnCCW(false);
      if(key == KeyEvent.VK_UP)
         ship.setUp(false);
      if(key == KeyEvent.VK_SPACE)
         isShooting = false;
   }
   
   
   public void readHighScore()
   {
      File filePath = new File(highScoreFile);
      try{
         Scanner dataGetter = new Scanner(filePath);
         highScore = dataGetter.nextInt();
         dataGetter.close();
      }
      catch (FileNotFoundException e){
         System.out.println("Creating high score file.");
         try
         {
            PrintWriter pw = new PrintWriter(new FileWriter(highScoreFile, false));
            pw.println(0);
            pw.close();
         } catch (IOException e1){e1.printStackTrace();}
      }
   }
   public void setScores()
   {
      if(score > highScore)
      {
         highScore = score;
         try

         {
            System.out.println("Writing high score.");
            PrintWriter pw = new PrintWriter(new FileWriter(highScoreFile, false));
            pw.println(highScore);
            pw.close();
         } catch (IOException e1){e1.printStackTrace();}
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
     if(!paused) 
     {
         if(ship.isDead())
            cDeath++;
         
         update();
         ship.update();
         
         if(!ship.isDead())
            shoot();
         
         for(int i = 0; i < b.size(); i++)
            b.get(i).update();
         
         for(int i = 0; i < ast.size(); i++)
            ast.get(i).update();  
     }
     changeMusic();
     dontLetTheBulletsDissapear();
     repaint();
   }
}