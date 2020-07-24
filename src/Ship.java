import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

//Jarrett Bierman
//Program Description
//Sep 7, 2017
/**
 * The main player in the Asteroids game. 
 * It moves with a center point and three rotating points to emulate a triangle 
 * :)
 * 
 * @author Jarrett Bierman
 *
 */
public class Ship
{
   private double dDeg;
   private double stopSpeed, dx, dy, rotateSpeed, degree, rotateStopSpeed, rotDeg;
   private double currentSpeed, maxSpeed;  
   private double time, distance;
   private boolean up, down, left, right, rDirection, lDirection, turnCW, turnCCW;
   private boolean dead;
   private Point topP, leftP, rightP, centerP;
   private static final int PREF_W = 1200;
   private static final int PREF_H = 750;
   private double cpx; 
   private double cpy; 
   private Color c;
   private int cR, cG, cB;
   private boolean r = true;
   private Image explosion;

   public Ship(int x, int y)
   {
      centerP = new Point(x,y);
      topP = new Point(0,0);
      leftP = new Point(0,0);
      rightP = new Point(0,0);
      
      cpx = x;
      cpy = y;
      
      rotDeg = 0;
      
      rotateStopSpeed = 2;
      rotateSpeed = 2;
      
      stopSpeed = 0.02;
      currentSpeed = 0;
      maxSpeed = 2.5;
      
      dead = false;
      
      try
      {explosion = new ImageIcon(this.getClass().getResource("explosion1.gif")).getImage();}
      catch (NullPointerException e){e.printStackTrace();} 
      
//      try{
//      bg = new ImageIcon(this.getClass().getResource("explosion.gif")).getImage();}
//   catch (NullPointerException e){e.printStackTrace();}
   }
   
   public void drawWhite(Graphics2D g2)
   {
      g2.setColor(Color.LIGHT_GRAY);
      g2.fill(getBounds());
   }
   
   public void drawColor(Graphics2D g2)
   {
      g2.setColor(c);
      g2.fill(getBounds());
   }
   
   public void drawChill(Graphics2D g2)
   {
      g2.setColor(new Color(255,66,14));
      g2.fill(getBounds());
   }
   
   public void drawExplsion(Graphics2D g2)
   {
      g2.drawImage(explosion, leftP.x - 30, topP.y - 60, 150, 150, null);
   }
   
   public Point getCenter()
   {
      return centerP;
   }
   
   public Point getTop()
   {
      return topP;
   }
   
   public Point getBottomMiddle()
   {
      return new Point((leftP.x + rightP.x)/2, (leftP.y + rightP.y)/2);
   }
   
   public Point getRightMiddle()
   {
      return new Point((leftP.x + topP.x)/2, (leftP.y + topP.y)/2);
   }
   
   public Point getLeftMiddle()
   {
      return new Point((topP.x + rightP.x)/2, (topP.y + rightP.y)/2);
   }
   
   
   public boolean intersectsAst(Asteroid ast)
   {
      Point[] points= 
         {
            topP, leftP, rightP, centerP,
            getBottomMiddle(), getLeftMiddle(), getRightMiddle()
         };
      for(int i = 0; i < points.length; i++)
         if(ast.getBounds().contains(points[i]))
            return true;
      return false;
   }
   
   
   public void update()
   {     
   // double x1 = getCenter().x + 1;
    double x1 = centerP.x + 1;
   // double y1 = getCenter().y + getSlope();
    double y1 = centerP.y + getSlope();
    Point p = new Point((int)x1, (int)y1);
   // double dist = p.distance(getCenter());
    double dist = p.distance(centerP);
    double time = dist / currentSpeed;
    
    if(up)
    {
      if(!dead)
      {
          //speeding up
          if(currentSpeed < maxSpeed)
             currentSpeed += stopSpeed*5;
          else
             currentSpeed = maxSpeed;
          
          if(degree == 0 || degree == 180 || degree == 360)
          {
             dx = 0;
             if(degree >= 180)
                dy = currentSpeed;
             else
                dy = -currentSpeed;
          }
          else
          {
             dx = Math.abs((x1 - centerP.x)) / time;
             dy = Math.abs((y1 - centerP.y)) / time;
          }
      
         if(degree > 0 && degree <= 90)
         {
            dy = -dy;
         }
         else if(degree > 90 && degree <= 180)
         {
            
         }
         else if(degree > 180 && degree <= 270)
         {
            dx = -dx;
         }
         else if(degree > 270 && degree <= 360)
         {
            dx = -dx;
            dy = -dy;
         }
      }
    }
   //\\//\\//\\//\\//\\//\\//\\//\\//\\//\/\/\//\/\/\/\\
   else
   {
      if(dx < 1 && dy < 1)
         currentSpeed = 0;
         
         
      if(dx > 0)
         dx -= stopSpeed;
      else if(dx < 0)
         dx += stopSpeed;
      else
         dx = 0;
      
      
      if(dy > 0)
         dy -= stopSpeed;
      else if(dy < 0)
         dy += stopSpeed;
      else
         dy = 0;
   }
    
    centerP.x = (int)cpx;
    centerP.y = (int)cpy;
    cpx += dx;
    cpy += dy;
    
         
    if(cpx < 0 - 30)
       cpx= PREF_W + 30;
    if(cpx > PREF_W + 30)
       cpx = 0 - 30;
    
    if(cpy < 0 - 30)
       cpy = PREF_H + 30;
    if(cpy > PREF_H + 30)
       cpy = 0 - 30;

   rotate();
   changeColor();
   
         
   }   
   
   public void rotate()
   {
      if(turnCW)
      {
         if(!dead)
         {
            rotDeg += rotateSpeed;
            if(degree >= 360)
               degree = 0;
            else
               {
//                  dDeg += moveSpeed;
                  if(dDeg > maxSpeed)
                     dDeg = maxSpeed;
                  dDeg = rotateSpeed;
               }
         }
      }
      
      else if(turnCCW)
      {
         if(!dead)
         {
            if(degree < 0)
               degree = 360;
            else
            {
//               dDeg -= moveSpeed;
               if(dDeg < -maxSpeed)
                  dDeg = -maxSpeed;
               dDeg = -rotateSpeed;
            }
            rotDeg -= rotateSpeed;
         }
      }
      else if(dDeg > 0)
      {
         dDeg -= rotateStopSpeed;
         if(dDeg < 0)
            dDeg = 0;
      }
      else if(dDeg < 0)
      {
         dDeg += rotateStopSpeed;
         if(dDeg > 0)
            dDeg = 0;
      }
      
      degree += dDeg;
      
      if(degree < 0)
         degree = 360 + degree;
      if(degree > 360)
         degree = 0 + (360 - degree);
      
      double turn = 1.745; //JUST GOTTA HARD CODE IT!!!
      topP.x = (int) -(0.35*(Math.sin((rotDeg*turn + 314)% 628.0 / 100) * 100)) + centerP.x;
      topP.y = (int)  (0.35*(Math.cos((rotDeg*turn + 314)% 628.0 / 100) * 100)) + centerP.y;
      
      rightP.x = (int) -(0.3*(Math.sin((rotDeg*turn - 80)% 628.0 / 100) * 100)) + centerP.x;
      rightP.y = (int)  (0.3*(Math.cos((rotDeg*turn - 80)% 628.0 / 100) * 100)) + centerP.y;
      
      leftP.x = (int) -(0.3*(Math.sin((rotDeg*turn + 80)% 628.0 / 100) * 100)) + centerP.x;
      leftP.y = (int)  (0.3*(Math.cos((rotDeg*turn + 80)% 628.0 / 100) * 100)) + centerP.y;
   }

   public void changeColor()
   {
      c = new Color(cR, 255 - cR, 100);
      
      if(cR == 0)
         r = true;
      if(cR == 255)
         r = false;
      if(r == true) cR+=5;
      else  cR-=5;
   }
   
   public double getSlope()
   {
      return 1 / Math.tan(Math.toRadians(degree));
   }
   
   
   public Shape getBounds()
   {
      int[] xs = {topP.x, leftP.x, rightP.x};
      int[] ys = {topP.y, leftP.y, rightP.y};
      return new Polygon(xs, ys, 3);
   }

   public double getStopSpeed()
   {
      return stopSpeed;
   }

   public void setStopSpeed(double stopSpeed)
   {
      this.stopSpeed = stopSpeed;
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

   public boolean isUp()
   {
      return up;
   }

   public void setUp(boolean up)
   {
      this.up = up;
   }

   public boolean isDown()
   {
      return down;
   }

   public void setDown(boolean down)
   {
      this.down = down;
   }

   public boolean isLeft()
   {
      return left;
   }

   public void setLeft(boolean left)
   {
      this.left = left;
   }

   public boolean isRight()
   {
      return right;
   }

   public void setRight(boolean right)
   {
      this.right = right;
   }

   public boolean isrDirection()
   {
      return rDirection;
   }

   public void setrDirection(boolean rDirection)
   {
      this.rDirection = rDirection;
   }

   public boolean islDirection()
   {
      return lDirection;
   }

   public void setlDirection(boolean lDirection)
   {
      this.lDirection = lDirection;
   }

   public double getDegree()
   {
      return degree;
   }

   public void setDegree(int degree)
   {
      this.degree = degree;
   }

   public boolean isTurnCW()
   {
      return turnCW;
   }

   public void setTurnCW(boolean turnCW)
   {
      this.turnCW = turnCW;
   }

   public boolean isTurnCCW()
   {
      return turnCCW;
   }

   public void setTurnCCW(boolean turnCCW)
   {
      this.turnCCW = turnCCW;
   }
   
   public double getTime()
   {
      return time;
   }

   public void setTime(double time)
   {
      this.time = time;
   }

   public double getDistance()
   {
      return distance;
   }

   public void setDistance(double distance)
   {
      this.distance = distance;
   }

   public boolean isDead()
   {
      return dead;
   }

   public void setDead(boolean dead)
   {
      this.dead = dead;
   }
}