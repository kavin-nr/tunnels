import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class HoverImage
{
   private ImageIcon regular;
   private ImageIcon hover;
   private int x;
   private int y;
   private int width;
   private int height;
   private BufferedImage img;
   private Graphics gr;
   
   public HoverImage(int xV, int yV, int wV, int hV, String regularFilename, String hoverFilename)
   {
      x = xV;
      y = yV;
      width = wV;
      height = hV;
      
      regular = new ImageIcon(regularFilename);
      hover = new ImageIcon(hoverFilename);
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(regular.getImage(), 0, 0, width, height, null);
   }
   
   public BufferedImage getImage()
   {
      return img;
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
   
   public void switchHover()
   {
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(hover.getImage(), 0, 0, width, height, null);
   }
   
   public void noHover()
   {
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(regular.getImage(), 0, 0, width, height, null);
   }
   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}
   