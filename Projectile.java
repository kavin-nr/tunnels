import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class Projectile implements Animatable
{
   private int x, y, width, height, damage, dX;
   private ImageIcon projectileSrc;
   private BufferedImage img;
   private Graphics gr;
   
   private String projectilePath;
   
   public Projectile(int xValue, int yValue, int widthValue, int heightValue, String projectilePathValue, int damageValue)
   {
      x = xValue;
      y = yValue;
      width = widthValue;
      height = heightValue;
      damage = damageValue;
      
      projectilePath = projectilePathValue;
      projectileSrc = new ImageIcon(projectilePath);
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(projectileSrc.getImage(), 0, 0, width, height, null);
            
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
   
   public int getDamage()
   {
      return damage;
   }
   
   public int getDX()
   {
      return dX;
   }
   
   public String getProjectilePath()
   {
      return projectilePath;
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
   
   public void setDamage(int damageValue)
   {
      damage = damageValue;
   }
   
   public void setDX(int dXValue)
   {
      dX = dXValue;
   }
   
   public void step()
   {
      x += dX;
   }
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}     