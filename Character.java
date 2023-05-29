import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Character implements Animatable
{
   private int x, y, width, height, dX, dY, health;
   
   private int changeFrame = 0;
      
   private ImageIcon frame1, frame2;
   
   private ImageIcon frame1l = new ImageIcon("img/sprites/Knight1L.png");
   private ImageIcon frame2l = new ImageIcon("img/sprites/Knight2L.png");
   
   private ImageIcon frame1r = new ImageIcon("img/sprites/Knight1R.png");
   private ImageIcon frame2r = new ImageIcon("img/sprites/Knight2R.png");
   
   private ImageIcon frame1u = new ImageIcon("img/sprites/Knight1U.png");
   private ImageIcon frame2u = new ImageIcon("img/sprites/Knight2U.png");
   
   private ImageIcon frame1d = new ImageIcon("img/sprites/Knight1D.png");
   private ImageIcon frame2d = new ImageIcon("img/sprites/Knight2D.png");
   
   private BufferedImage img; 
   private Graphics bufG;
   
   private JPanel owner;
   
   // constructors
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
   
   public int getDX()
   {
      return dX;
   }
   
   public int getDY()
   {
      return dY;
   }
   
   public int getHealth()
   {
      return health;
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
   
   public void setDX(int dXValue)
   {
      dX = dXValue;
   }
   
   public void setDY(int dYValue)
   {
      dY = dYValue;
   }
   
   public void setHealth(int hValue)
   {
      health = hValue;
   }
   
   public void faceLeft()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1l;
      frame2 = frame2l;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
   }
   
   public void faceRight()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1r;
      frame2 = frame2r;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );     
   } 
   
   public void faceAhead()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1u;
      frame2 = frame2u;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
   }
   
   public void faceBack()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = frame1d;
      frame2 = frame2d;
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );     
   }
     
   //instance methods
   public void step()  //Implement Animatable's required step()
   {
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
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}