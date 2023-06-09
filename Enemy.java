import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.lang.Math;

/**
* Class that handles all the different enemies and their attacks, and makes them show up in the world and battles.
*/
public class Enemy implements Animatable
{
   /**
   * Constructor that handles enemy position, health, and how far they can spot you from
   */
   private int x, y, width, height, visibility, health;
   /**
   * Sets up the first frame of the enemy
   */
   private ImageIcon frameOne;
   /**
   * Sets up the second frame of the enemy
   */      
   private ImageIcon frameTwo;
   /**
   * Sets up a buffered image
   */      
   private BufferedImage img;
   /**
   * Sets up graphics
   */   
   private Graphics gr;
   /**
   * Sets up the enemy's projectile
   */      
   private Projectile projectile, ammo;
   /**
   * Accesses the file of the first frame
   */         
   private String frameOnePath;
   /**
   * Accesses the file of the second frame
   */         
   private String frameTwoPath;
   /**
   * Accesses the name of the enemy
   */         
   private String name;
   /**
   * Changes the frames to simulate movement
   */        
   private int changeFrame = (int) (Math.random() * 50);
   
   /**
   * Initializes the enemy
   */      
   public Enemy(String nameValue, int xValue, int yValue, int widthValue, int heightValue, int visibilityValue, String frameOnePathValue, String frameTwoPathValue, Projectile projectileValue, Projectile ammoValue)
   {
      name = nameValue;
      x = xValue;
      y = yValue;
      width = widthValue;
      height = heightValue;
      visibility = visibilityValue;
      health = 100;
      
      frameOnePath = frameOnePathValue;
      frameTwoPath = frameTwoPathValue;
      frameOne = new ImageIcon(frameOnePath);
      frameTwo = new ImageIcon(frameTwoPath);
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(frameOne.getImage(), 0, 0, width, height, null);
      
      projectile = projectileValue;
      ammo = ammoValue;    
   }
   
     
   //accessors
   /**
   * Access the name of the enemy
   */   
   public String getName()
   {
      return name;
   }
   /**
   * Gets the x value
   */   
   public int getX()
   {
      return x;
   }
   /**
   * Gets the y value
   */   
   
   public int getY()
   {
      return y;
   }
   /**
   * Gets the width
   */   
   
   public int getWidth()
   {
      return width;
   }
   /**
   * Gets the height
   */   
   
   public int getHeight()
   {
      return height;
   }
   /**
   * Gets the visibility value
   */      
   public int getVisibility()
   {
      return visibility;
   }
   /**
   * Gets the health
   */   
   
   public int getHealth()
   {
      return health;
   }
   /**
   * Gets the file for the first frame
   */   
   
   public String getFrameOnePath()
   {
      return frameOnePath;
   }
   /**
   * Gets the file for the second frame
   */   
   public String getFrameTwoPath()
   {
      return frameTwoPath;
   }
   /**
   * Gets the projectile
   */   
   
   public Projectile getProjectile()
   {
      return projectile;
   }
   /**
   * Gets the ammo
   */   
   
   public Projectile getAmmo()
   {
      return ammo;
   }

   //modifiers
   /**
   * Modifies the x value
   */   
   
   public void setX(int xv)
   {
      x = xv;
   }
   /**
   * Modifies the y value
   */   
   
   public void setY(int yv)
   {
      y = yv;
   }
   /**
   * Modifies the width
   */   
  
   public void setWidth(int wv)
   {
      width = wv;
   }
   /**
   * Modifies the height
   */   
   
   public void setHeight(int hv)
   {
      height = hv;
   }
   /**
   * Modifies the visibility
   */   
   
   public void setVisibility(int vv)
   {
      visibility = vv;
   }
   /**
   * Modifies the health
   */   
   
   public void setHealth(int hValue)
   {
      health = hValue;
   }
   /**
   * Modifies the file for the first frame
   */   

   public void setFrameOnePath(String path)
   {
      frameOnePath = path;
   }
   /*
   * Modifies the file for the second frame
   */   

   public void setFrameTwoPath(String path)
   {
      frameTwoPath = path;
   }
   /**
   * Modifies the projectile
   */   
   
   public void setProjectile(Projectile projValue)
   {
      projectile = projValue;
   }
   /**
   * Modifies the ammo
   */   
   
   public void setAmmo(Projectile am)
   {
      ammo = am;
   }
   /**
   * Changes the frame continuously to simulate movement
   */   
   
   public void step()
   {
      if (changeFrame % 50 > 30)
      {
         gr.drawImage(frameOne.getImage(), 0, 0, width, height, null);
      }
      else
      {
         gr.drawImage(frameTwo.getImage(), 0, 0, width, height, null);
      }
      changeFrame++;
   }
   /**
   * Draws the image
   */   
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}     