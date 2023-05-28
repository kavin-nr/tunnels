import javax.swing.*;
import java.awt.*;

public class Tunnels
{
   public static void main(String[] args)
   { 
      JFrame frame = new JFrame("Tunnels");
      frame.setLocation(200, 0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new TunnelsPanel(frame));
      frame.setResizable(false);
      frame.pack();                                                     
      frame.setVisible(true);
      
      frame.setIconImage((new ImageIcon("img/sprites/Knight1R.png")).getImage());
   }
}