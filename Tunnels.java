import javax.swing.*;
import java.awt.*;

public class Tunnels
{
    public static void main(String[] args)
   { 
      JFrame frame = new JFrame("Tunnels");
      frame.setLocation(400, 50);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new TunnelsPanel());  
      frame.pack();                                                     
      frame.setVisible(true);
   }
}