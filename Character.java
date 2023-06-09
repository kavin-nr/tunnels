import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
* This class adds the playable character into the game
*/

public class Character implements Animatable
{
   /**
   * Set the characters position, dimensions, and height.
   */
   private int x, y, width, height, dX, dY, health;
  
   /**
   * Counter for every time the frame changes
   */
   
   private int changeFrame = 0;
   
   /**
   * sets up an image icon with two frames
   */
   private ImageIcon frame1, frame2;
   /**
   * Sets up the first frame for when the knight is facing left
   */ 
   private ImageIcon frame1l = new ImageIcon("img/sprites/Knight1L.png");
   /**
   * Sets up the second frame for when the knight is facing left
   */  
   private ImageIcon frame2l = new ImageIcon("img/sprites/Knight2L.png");
   /**
   * Sets up the first frame for when the knight is facing right
   */     
   private ImageIcon frame1r = new ImageIcon("img/sprites/Knight1R.png");
   /**
   * Sets up the second frame for when the knight is facing right
   */
   private ImageIcon frame2r = new ImageIcon("img/sprites/Knight2R.png");
   /**
   * Sets up the first frame for when the knight is facing up
   */
   private ImageIcon frame1u = new ImageIcon("img/sprites/Knight1U.png");
   /**
   * Sets up the second frame for when the knight is facing up
   */
   private ImageIcon frame2u = new ImageIcon("img/sprites/Knight2U.png");
   /**
   * Sets up the first frame for when the knight is facing down
   */   
   private ImageIcon frame1d = new ImageIcon("img/sprites/Knight1D.png");
   /**
   * Sets up the second frame for when the knight is facing down
   */
   private ImageIcon frame2d = new ImageIcon("img/sprites/Knight2D.png");
   /**
   * Sets up a buffered image 
   */      
   private BufferedImage img; 
   /**
   * Sets up graphics
   */
   private Graphics bufG;
   /**
   * Sets up a JPanel to become an "owner" or parent panel. 
   */   
   private JPanel owner;
   
   /**
   * Sets the dimensions and position of the character when they load into the main world
   */
   public Character(WorldPanel o)
   {
      owner = o;
      x = 390;
      y = 555;
      width = 75;
      height = 75;
      health = 100;
      dX = 0;
      dY = 0;
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      bufG = img.getGraphics();
      frame1 = frame1u;
      frame2 = frame2u;
   }
   /**
   * Sets the dimensions and position of the character when they are in combat
   */   
   public Character(CombatPanel o)
   {
      owner = o;
      x = 440;
      y = 360;
      width = 50;
      height = 50;
      dX = 0;
      dY = 0;
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      bufG = img.getGraphics();
      frame1 = frame1l;
      frame2 = frame2l;
   }
      
      
   /**
   Acessor to get x value
   */
   public int getX()
   {
      return x;
   }
   /**
   Acessor to get y value
   */   
   public int getY()
   {
      return y;
   }
   /**
   Acessor to get width
   */   
   public int getWidth()
   {
      return width;
   }
   /**
   Acessor to get height
   */
   
   public int getHeight()
   {
      return height;
   }
   /**
   Acessor to get x direction
   */   
   public int getDX()
   {
      return dX;
   }
   /**
   Acessor to get y direction
   */   
   public int getDY()
   {
      return dY;
   }
   /**
   Acessor to get health 
   */   
   public int getHealth()
   {
      return health;
   }
   
   /**
   * Modifier to set x value 
   */
   public void setX(int xv)
   {
      x = xv;
   }
   /**
   * Modifier to set y value 
   */
   public void setY(int yv)
   {
      y = yv;
   }
   /**
   * Modifier to set width
   */   
   public void setWidth(int wv)
   {
      width = wv;
   }
   /**
   * Modifier to set height 
   */   
   public void setHeight(int hv)
   {
      height = hv;
   }
   /**
   * Modifier to set x direction 
   */
   public void setDX(int dXValue)
   {
      dX = dXValue;
   }
   /**
   * Modifier to set y direction
   */
   public void setDY(int dYValue)
   {
      dY = dYValue;
   }
   /**
   * Modifier to set health
   */  
   public void setHealth(int hValue)
   {
      health = hValue;
   }
   /**
   * Changes the frames to make the character face left
   */
   
   public void faceLeft()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1l;
      frame2 = frame2l;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
   }
   /**
   * Changes the frames to make the character face right
   */   
   public void faceRight()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1r;
      frame2 = frame2r;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );     
   } 
   /**
   * Changes the frames to make the character face forward
   */   
   public void faceAhead()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1u;
      frame2 = frame2u;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
   }
   /**
   * Changes the frames to make the character face backwards
   */   
   public void faceBack()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1d;
      frame2 = frame2d;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );     
   }
     
   /**
   * Instance methods that stop you from going off the map, and update the character to be facing right, left, up, or down based on which direction they are moving.
   */
   public void step()  //Implement Animatable's required step()
   {
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();

      if (!((x < 10 && dX < 0) || (owner.getWidth() - this.width - 10 < x && dX > 1)))
      {
         x += dX;
      }
      if (!((y < 10 && dY < 0) || (owner.getHeight() - this.height - 10 < y && dY > 1)))
      {
         y += dY;
      }
      
      if (dX > 0)
      {
         faceRight();
      }
      else if (dX < 0)
      {
         faceLeft();
      }
      
      else if (dY > 0)
      {
         faceBack();
      }
      else if (dY < 0)
      {
         faceAhead();
      }
      
      if (changeFrame % 100 > 50)
      {
         bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
      }
      else
      {
         bufG.drawImage( frame2.getImage() , 0 , 0 , width , height , null );
      }
      changeFrame ++;
   }
   /**
   * Draws the image
   */
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}