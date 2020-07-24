import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.Timer;

//Jarrett Bierman
//Program Description
//Sep 12, 2017

public class Bullet implements ActionListener
{
   private int SPEED;
   private double x, y, dx, dy, degree, size;
   private boolean isTimeUp;
   private static final int PREF_W = 1200;
   private static final int PREF_H = 750;
   private Timer timer;
   private int c, cColor;
   private Color [] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};

   
   public Bullet(double x, double y, double degree)
   {
      this.degree = degree - 90;
      this.x = x;
      this.y = y;
      this.SPEED = 8;
      this.size = 10;
      isTimeUp = false;  
      
      cColor = (int)(Math.random() * colors.length);
      
      timer = new Timer(1000, this);
//      timer.start();
   }
   
   
   public void update()
   {    
//univeral speed * sin theta = dy.    same thing but with cos with dx
      dx = SPEED * Math.cos(Math.toRadians(degree));
      dy = SPEED * Math.sin(Math.toRadians(degree));
      x += dx;
      y += dy;
      
      if(x > PREF_W + size)
         x = 0 - size;
      if(x < 0 - size)
         x = PREF_W + size;
      
      if(y > PREF_H + size)
         y = 0 - size;
      if(y < 0 - size)
         y = PREF_H + size;
      
      if(c >= 3)
         isTimeUp = true;     
   }
   
   public void bulletCountDown()
   {
      c++;
      if(c >= 5)
         timer.stop();
   }
   
   public void drawBulletColor(Graphics2D g2)
   {
      g2.setColor(colors[cColor]);
      g2.fill(getBounds());
   }
   
   public void drawBulletWhite(Graphics2D g2)
   {
      g2.setColor(Color.GRAY);
      g2.fill(getBounds());
   }
   
   public void drawBulletChill(Graphics2D g2)
   {
      g2.setColor(Color.YELLOW);
      g2.fill(getBounds());
      
   }
   
   public Ellipse2D.Double getBounds()
   {
      return new Ellipse2D.Double((int)x - (size / 2), (int)y - (size / 2), size, size);
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      bulletCountDown();
   }
 
//________________________________________________________________________________________________//   
   
   
   public int getSPEED()
   {
      return SPEED;
   }

   public void setSPEED(int sPEED)
   {
      SPEED = sPEED;
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

   public boolean isTimeUp()
   {
      return isTimeUp;
   }

   public void setTimeUp(boolean isTimeUp)
   {
      this.isTimeUp = isTimeUp;
   }

   public Timer getTimer()
   {
      return timer;
   }

   public void setTimer(Timer timer)
   {
      this.timer = timer;
   }

}
