import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

//Jarrett Bierman
//Program Description
//Oct 26, 2017

public class Asteroid
{
   private double x, y, width, height, dx, dy;
   private int stage;
   private static final int PREF_W = 1200;
   private static final int PREF_H = 750;
   private Image [] imgs;
   private Image img;
   private int degree;
   private int rotateSpeed;
   
   
   public Asteroid(int x, int y, double dx, double dy, double width, double height, int stage)
   {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
      this.width = width;
      this.height = height;
      this.stage = stage;
      
      imgs = new Image[13];
      try
      {imgs[0] = new ImageIcon(this.getClass().getResource("circle1.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();} 
      try
      {imgs[1] = new ImageIcon(this.getClass().getResource("circle2.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();} 
      try
      {imgs[2] = new ImageIcon(this.getClass().getResource("circle3.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();} 
      try
      {imgs[3] = new ImageIcon(this.getClass().getResource("circle4.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();} 
      try
      {imgs[4] = new ImageIcon(this.getClass().getResource("circle5.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[5] = new ImageIcon(this.getClass().getResource("circle6.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[6] = new ImageIcon(this.getClass().getResource("cirlcle7.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[7] = new ImageIcon(this.getClass().getResource("circle8.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[8] = new ImageIcon(this.getClass().getResource("circle9.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[9] = new ImageIcon(this.getClass().getResource("circle10.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[10] = new ImageIcon(this.getClass().getResource("circle11.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[11] = new ImageIcon(this.getClass().getResource("circle12.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      try
      {imgs[12] = new ImageIcon(this.getClass().getResource("circle13.png")).getImage();}
      catch (NullPointerException e){e.printStackTrace();}
      
      this.img = imgs[(int)(Math.random() * imgs.length)];
  
      degree = (int)(Math.random() * 360);
      rotateSpeed = (int)(Math.random()*10) - 5;
   }
   
   public Point center()
   {
      return new Point((int)x + ((int)width/2), (int)y + ((int)height / 2));
   }
   
   
   public Shape getBounds()
   {
      return new Ellipse2D.Double((int)x, (int)y, (int)width, (int)height);
   }
   
   public void drawAst(Graphics2D g2)
   {     
      g2.setColor(Color.WHITE);
      g2.fillOval((int)x, (int)y, (int)width, (int)height);
   }
   
   public void drawAstPic(Graphics2D g2)
   {
      AffineTransform rot = new AffineTransform();
      rot.rotate(Math.toRadians(degree), center().x, center().y);
      g2.setTransform(rot);
      g2.drawImage(img, (int)x, (int)y, (int)width, (int)height, null);
      
      if(degree < 360 || degree >= 0)
         degree += rotateSpeed;
      else if(degree > 360)
         degree = 0;
      else if(degree < 0)
         degree = 360;
   }
   
   public void drawAstChill(Graphics2D g2)
   {
      g2.setColor(new Color(0, 0, 100));
      g2.fillOval((int)x, (int)y, (int)width, (int)height);
   }
   
   public void update()
   {
      x += dx;
      y += dy;
      
      
      if(x < 0 - width)         x = PREF_W + width;
      if(x > PREF_W + width)    x = 0 - width;
      
      if(y < 0 - height)         y = PREF_H + height;
      if(y > PREF_H + height)    y = 0 - height;
      
   }
 
   public double getX()
   {
      return x;
   }

   public void setX(double x)
   {
      this.x = x;
   }

   public double getY()
   {
      return y;
   }

   public void setY(double y)
   {
      this.y = y;
   }

   public double getWidth()
   {
      return width;
   }

   public void setWidth(double width)
   {
      this.width = width;
   }

   public double getHeight()
   {
      return height;
   }

   public void setHeight(double height)
   {
      this.height = height;
   }

   public double getDx()
   {
      return dx;
   }

   public void setDx(double dx)
   {
      this.dx = dx;
   }

   public double getDy()
   {
      return dy;
   }

   public void setDy(double dy)
   {
      this.dy = dy;
   }

   public int getStage()
   {
      return stage;
   }

   public void setStage(int stage)
   {
      this.stage = stage;
   }
   
}