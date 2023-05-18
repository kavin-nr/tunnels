import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class Enemy implements Animatable
{
   private int x, y, width, height, visibility;
   private ImageIcon frameOne;
   private ImageIcon frameTwo;
   private BufferedImage img;
   private Graphics gr;
   private Projectile projectile;
   
   private int changeFrame;
   
   public Enemy(int xValue, int yValue, int widthValue, int heightValue, int visibilityValue, String frameOnePath, String frameTwoPath, Projectile projectileValue)
   {
      x = xValue;
      y = yValue;
      width = widthValue;
      height = heightValue;
      visibility = visibilityValue;
      
      frameOne = new ImageIcon(frameOnePath);
      frameTwo = new ImageIcon(frameTwoPath);
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(frameOne.getImage(), 0, 0, width, height, null);
      
      projectile = projectileValue;    
   }
   
     
   //accessors
   public int getX()
   {
      return x;
   }
   
   public int getY()
   {
      return y;
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   public int getVisibility()
   {
      return visibility;
   }
   
   public Projectile getProjectile()
   {
      return projectile;
   }

   //modifiers
   public void setX(int xv)
   {
      x = xv;
   }
   
   public void setY(int yv)
   {
      y = yv;
   }
   
   public void setWidth(int wv)
   {
      width = wv;
   }
   
   public void setHeight(int hv)
   {
      height = hv;
   }
   
   public void setVisibility(int vv)
   {
      visibility = vv;
   }
   
   public void setProjectile(Projectile projValue)
   {
      projectile = projValue;
   }
   
   public void step()
   {
      if (changeFrame % 50 > 33)
      {
         gr.drawImage(frameOne.getImage(), 0, 0, width, height, null);
      }
      else
      {
         gr.drawImage(frameTwo.getImage(), 0, 0, width, height, null);
      }
      changeFrame++;
   }
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}     