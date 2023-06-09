import java.awt.*;
import javax.swing.*;

/**
*Class to display title screen
*/
public class Title implements Animatable
{
   /**
   * Initializes y value
   */

   private int y;
   /**
   * Initializes y direction
   */
   
   private int dY;
   /**
   * Initializes an image icon for background
   */
   private ImageIcon src;
   private int c = 0;
   /**
   * Initializes title panel as the owner
   */   
   private TitlePanel owner;

   /**
   * Initializes the title
   */
  
   public Title(TitlePanel o)
   {
      owner = o;
      y = 0;
      dY = 1;
      src = new ImageIcon("img/title/Title.png");
   }
   
   public void step()
   {
      if (c % 10 == 0)
      {
         if (y < 0 || 20 < y)
         {
            dY = -dY;
         }
         y += dY;
      }
      c++;
   }
   /**
   * Draws the image
   */
   
   public void drawMe(Graphics g)
   {
      g.drawImage(src.getImage(), 0, y, owner.getWidth(), owner.getHeight(), null);
   }
}