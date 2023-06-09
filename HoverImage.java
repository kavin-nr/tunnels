import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

/**
* Class that creates the "hovering" buttons which turn a different color when the mouse is placed over them.
*/



public class HoverImage
{
   /**
   * Sets up the regular image icon
   */

   private ImageIcon regular;
   /**
   * Sets up the hover image icons
   */
   private ImageIcon hover;
   /**
   * Sets up x value
   */   
   private int x;   
   /**
   * Sets up y value
   */
   private int y;
   /**
   * Sets up width
   */   
   private int width;
   /**
   * Sets up height
   */   
   private int height;
   /**
   * Sets up a buffered image
   */
   
   private BufferedImage img;
   /**
   * Sets up graphics
   */
   
   private Graphics gr;
   /**
   * Sets up hover images
   */
   
   public HoverImage(int xV, int yV, int wV, int hV, String regularFilename, String hoverFilename)
   {
      //Sets dimensions
      x = xV;
      y = yV;
      width = wV;
      height = hV;
      //Sets the files for regular and hover
      regular = new ImageIcon(regularFilename);
      hover = new ImageIcon(hoverFilename);
      
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(regular.getImage(), 0, 0, width, height, null);
   }
   /**
   * Gets the buffered image
   */
   
   public BufferedImage getImage()
   {
      return img;
   }
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
   * Handles the switch from hovering or not hovering over a button. 
   */
   
   public void switchHover()
   {
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(hover.getImage(), 0, 0, width, height, null);
   }
   /**
   * Image for button with no hover
   */
   
   public void noHover()
   {
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      gr = img.getGraphics();
      gr.drawImage(regular.getImage(), 0, 0, width, height, null);
   }
   /**
   * Draws image
   */   
   public void drawMe(Graphics g)
   {
      g.drawImage(img, x, y, width, height, null);
   }
}
   