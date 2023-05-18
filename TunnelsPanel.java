import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Math;


public class TunnelsPanel extends JPanel
{
   private TitlePanel title;
   private JFrame owner;
   private DialoguePanel dialogue;
   private WorldPanel world;
   private JPanel blackOverlay;
   private Timer timer;
   private int fadeCount;
   
   public TunnelsPanel(JFrame o)
   {
      owner = o;
      setLayout(new BorderLayout());
      title = new TitlePanel(this);
      add(title);
      
      dialogue = new DialoguePanel();
      
      world = new WorldPanel();
   }
   
   public void goWorld() {
    // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, world.getWidth(), world.getHeight());
      add(blackOverlay);
      remove(title);
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();

      
   }
   
   public void goSettings()
   {
      remove(title);
      //add(settings);
      repaint();
      revalidate();
      owner.pack();
      //settings.requestFocusInWindow();
   }
   
   private class FadeListener implements ActionListener
   {
      private int alpha = 0;
   
      public void actionPerformed(ActionEvent e) 
      {
         alpha += (int) (Math.pow(fadeCount, 2) / 10);
         fadeCount++;
         
         if (alpha >= 255) 
         {
         // When the alpha value reaches 255, remove the black overlay panel and add the WorldPanel
            remove(blackOverlay);
            add(world);
            repaint();
            revalidate();
            owner.pack();
            world.requestFocusInWindow();
            timer.stop();
         } 
         else 
         {
         // Otherwise, set the alpha value of the black overlay panel and repaint the panel
            blackOverlay.setBackground(new Color(0, 0, 0, alpha));
            System.out.println(alpha);
            repaint();
         }
      }
   }

}