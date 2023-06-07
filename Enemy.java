import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.lang.Math;

public class Enemy implements Animatable
{
   private int x, y, width, height, visibility, health;
   private ImageIcon frameOne;
   private ImageIcon frameTwo;
   private BufferedImage img;
   private Graphics gr;
   private Projectile projectile, ammo;
   
   private String frameOnePath;
   private String frameTwoPath;
   private String name;
   
   private int changeFrame = (int) (Math.random() * 50);
   
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

   public String getName()
   {
      return name;
   }
   
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
   
   public int getHealth()
   {
      return health;
   }
   
   public String getFrameOnePath()
   {
      return frameOnePath;
   }
   
   public String getFrameTwoPath()
   {
      return frameTwoPath;
   }
   
   public Projectile getProjectile()
   {
      return projectile;
   }
   
   public Projectile getAmmo()
   {
      return ammo;
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
   
   public void setHealth(int hValue)
   {
      health = hValue;
   }
   
   public void setFrameOnePath(String path)
   {
      frameOnePath = path;
   }
   
   public void setFrameTwoPath(String path)
   {
      frameTwoPath = path;
   }
   
   public void setProjectile(Projectile projValue)
   {
      projectile = projValue;
   }
   
   public void setAmmo(Projectile am)
   {
      ammo = am;
   }
   
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
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}     