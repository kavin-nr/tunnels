import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

/**
*Class for projectiles
*/
public class Projectile implements Animatable
{
   /**
   * Constructor for coordinates, dimensions, damage, direction, and speed
   */
   
   private int x, y, width, height, damage, dX, spawnSpeed, minSpeed, maxSpeed;
   /**
   * Image icon for left projectile
   */   
   private ImageIcon projectileSrc;
   /**
   * Image icon for right projectile
   */
   private ImageIcon rightProjectileSrc;
   /**
   * Sets up a buffered image
   */   
   private BufferedImage img;
   /**
   * Sets up graphics
   */   
   private Graphics gr;
   /**
   * Gets file paths to both frames
   */
   
   private String projectilePath, rightProjectilePath;
   /**
   * Initializes projectiles
   */
   
   public Projectile(int widthValue, int heightValue, String projectilePathValue, String rightProjectilePathValue, int damageValue, int spawnSpeedValue, int minSpeedValue, int maxSpeedValue)
   {
      x = 0;
      y = 0;
      width = widthValue;
      height = heightValue;
      damage = damageValue;
      spawnSpeed = spawnSpeedValue;
      minSpeed = minSpeedValue;
      maxSpeed = maxSpeedValue;      
      
      projectilePath = projectilePathValue;
      rightProjectilePath = rightProjectilePathValue;
      projectileSrc = new ImageIcon(projectilePath);
      rightProjectileSrc = new ImageIcon(rightProjectilePath);
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();            
   }
     
   //accessors
   
   /**
   * Gets x value
   */
   
   public int getX()
   {
      return x;
   }
   /**
   * Gets y value
   */
   
   public int getY()
   {
      return y;
   }
   /**
   * Gets width
   */
   
   public int getWidth()
   {
      return width;
   }
   /**
   * Gets height
   */
   
   public int getHeight()
   {
      return height;
   }
   /**
   * Gets projectile damage
   */
   
   public int getDamage()
   {
      return damage;
   }
   /**
   * Gets spawn speed of projectile
   */
   
   public int getSpawnSpeed()
   {
      return spawnSpeed;
   }
   /**
   * Gets minimum speed of projectile
   */
   
   public int getMinSpeed()
   {
      return minSpeed;
   }
   /**
   * Gets maximum speed of projectile
   */
   
   public int getMaxSpeed()
   {
      return maxSpeed;
   }
   /**
   * Gets projectile direction
   */
   
   public int getDX()
   {
      return dX;
   }
   /**
   * Gets filepath for left projectile
   */
   
   public String getProjectilePath()
   {
      return projectilePath;
   }
   /**
   * Gets filepath for right projection
   */

   public String getRightProjectilePath()
   {
      return rightProjectilePath;
   }
   
   //modifiers
   /**
   * Sets x value
   */
   
   public void setX(int xv)
   {
      x = xv;
   }
   /**
   * Sets y value
   */
   
   public void setY(int yv)
   {
      y = yv;
   }
   /**
   * Sets width
   */
   
   public void setWidth(int wv)
   {
      width = wv;
   }
   /**
   * Sets height
   */
   
   public void setHeight(int hv)
   {
      height = hv;
   }
   /**
   * Sets damage
   */
   
   public void setDamage(int damageValue)
   {
      damage = damageValue;
   }
   /**
   * Sets spawn speed
   */
   
   public void setSpawnSpeed(int speedValue)
   {
      spawnSpeed = speedValue;
   }
   /**
   * Sets minimum speed
   */
   
   public void setMinSpeed(int speedValue)
   {
      minSpeed = speedValue;
   }
   /**
   * Sets maximum speed
   */
   
   public void setMaxSpeed(int speedValue)
   {
      maxSpeed = speedValue;
   }
   /**
   * Sets projectile direction
   */
   
   public void setDX(int dXValue)
   {
      dX = dXValue;

      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      if (dX > 0)
      {
         gr.drawImage(projectileSrc.getImage(), 0, 0, width, height, null);
      }
      else
      {
         gr.drawImage(rightProjectileSrc.getImage(), 0, 0, width, height, null);
      }
   }
   /**
   * Makes the projectile move across the screen
   */
   
   public void step()
   {
      x += dX;
   }
   /**
   * Draws the image
   */
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }

}     