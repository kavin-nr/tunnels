import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.Math;

public class Savepoint
{
   private int x, y, width, height;
   private ImageIcon src;
   private BufferedImage img;
   private Graphics gr;
      
   
   public Savepoint(int xValue, int yValue, int widthValue, int heightValue, String path)
   {
      x = xValue;
      y = yValue;
      width = widthValue;
      height = heightValue;
      
      src = new ImageIcon(path);
            
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(src.getImage(), 0, 0, width, height, null);
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
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}     