import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;


public class Title implements Animatable
{
   private int y;
   private int dY;
   private ImageIcon src;
   private int c = 0;

   
   public Title()
   {
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
      g.drawImage(src.getImage(), 0, y, 550, 450, null);
   }
}