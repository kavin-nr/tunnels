import java.awt.*;
import javax.swing.*;


public class Title implements Animatable
{
   private int y;
   private int dY;
   private ImageIcon src;
   private int c = 0;
   private TitlePanel owner;

   
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
   
   public void drawMe(Graphics g)
   {
      g.drawImage(src.getImage(), 0, y, owner.getWidth(), owner.getHeight(), null);
   }
}