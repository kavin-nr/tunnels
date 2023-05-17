import java.awt.*;
import java.awt.image.*;
import java.io.File;
import javax.swing.*;
import javax.imageio.*;

//InflatingCircle extends YOUR Circle class and implements Animatable

//Animatable requires a step() method, which is in this file, and a
//drawMe(Graphics g) method, which you should already have in Circle

public class Character implements Animatable
{
   private int x;
   private int y;
   private int width;
   private int height;
   private int dX;
   private int dY;
   
   private int changeFrame = 0;
      
   private ImageIcon frame1 = new ImageIcon("img/sprites/Knight1.png");
   private ImageIcon frame2 = new ImageIcon("img/sprites/Knight2.png");
   
   private ImageIcon frame1r = new ImageIcon("img/sprites/Knight1.png");
   private ImageIcon frame2r = new ImageIcon("img/sprites/Knight2.png");
   
   private BufferedImage img; 
   private Graphics bufG;
   
   private WorldPanel owner;
   
   // constructors
   public Character(WorldPanel o)
   {
      owner = o;
      x = 245;
      y = 300;
      width = 100;
      height = 100;
      dX = 0;
      dY = 0;
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      bufG = img.getGraphics();
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
   
   public void faceLeft()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = new ImageIcon("img/sprites/Knight1.png");
      frame2 = new ImageIcon("img/sprites/Knight2.png");
      bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
   }
   
   public void faceRight()
   {
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufG = img.getGraphics();
      frame1 = new ImageIcon("img/sprites/Knight1R.png");
      frame2 = new ImageIcon("img/sprites/Knight2R.png");
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
   }
   
   public void drawMe(Graphics g)
   {
      
      if (changeFrame % 100 > 50)
      {
         bufG.drawImage( frame1.getImage() , 0 , 0 , width , height , null );
      }
      else
      {
         bufG.drawImage( frame2.getImage() , 0 , 0 , width , height , null );
      }
      changeFrame ++;
      g.drawImage(img, x, y, width, height, null);
   }
}