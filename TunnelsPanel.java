import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TunnelsPanel extends JPanel
{
   private TitlePanel title;
   private JFrame owner;
   private DialoguePanel dialogue;
   private WorldPanel world;
   private JPanel blackOverlay;
   private Timer timer;
   
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
      blackOverlay.setBounds(0, 0, 550, 450);
      remove(title);
      add(blackOverlay);
   
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
         alpha += 5;
         if (alpha >= 200) 
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
            add(blackOverlay);
            repaint();
         }
      }
   }

}