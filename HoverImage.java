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
   
   public HoverImage()
   {
      x = 200;
      y = 200;
      regular = new ImageIcon("img/sprites/Skeleton1.png");
      hover = new ImageIcon("ïmg/sprites/Skeleton2.png");
      width = regular.getIconWidth();
      height = regular.getIconHeight();
      
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(regular.getImage(), x, y, width, height, null);
   }
   
   public HoverImage(int xV, int yV, String regularFilename, String hoverFilename)
   {
      x = xV;
      y = yV;
      
      regular = new ImageIcon(regularFilename);
      hover = new ImageIcon(hoverFilename);
      width = regular.getIconWidth();
      height = regular.getIconHeight();
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(regular.getImage(), x, y, width, height, null);
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
   