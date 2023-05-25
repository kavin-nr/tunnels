import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Math;


public class TunnelsPanel extends JPanel
{
   private TitlePanel title;
   private JFrame owner;
   private DialoguePanel dialogue;
   public WorldPanel world;
   private JPanel blackOverlay;
   private Timer timer;
   
   private JPanel ready;
   public TunnelsPanel(JFrame o)
   {
      owner = o;
      setLayout(new BorderLayout());
      title = new TitlePanel(this);
      add(title);
      
      dialogue = new DialoguePanel();
      
      world = new WorldPanel(this);
   }
   
   public void goWorld() {
    // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, world.getWidth(), world.getHeight());
      add(blackOverlay);
      remove(title);
      ready = world;
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();

      
   }
   
   public void goCombat(Enemy e) 
   {
   // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, title.getWidth(), title.getHeight());
      add(blackOverlay);
      remove(world);
      ready = new CombatPanel(e, this);
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();
   }
   
   public void endCombat(boolean result)
   {
      if (result)
      {
         // Create the black overlay panel with 0% alpha
         blackOverlay = new JPanel();
         blackOverlay.setBounds(0, 0, title.getWidth(), title.getHeight());
         add(blackOverlay);
         // Removes the already existing ready panel which should be the existing CombatPanel and adds the existing WorldPanel
         remove(ready);
         ready = world;
         // Use a Timer to gradually increase the alpha value of the black overlay panel
         timer = new Timer(50, new FadeListener()); 
         timer.start();
         
      }
      else
      {
         // display game over
      }
   }
   
   private class FadeListener implements ActionListener
   {
      private int alpha = 0;
      private int fadeCount = 0;
      public void actionPerformed(ActionEvent e) 
      {
         alpha += (int) (Math.pow(fadeCount, 2) / 10);
         fadeCount++;
         
         if (alpha >= 255) 
         {
         // When the alpha value reaches 255, remove the black overlay panel and add the WorldPanel
            remove(blackOverlay);
            add(ready);
            repaint();
            revalidate();
            owner.pack();
            ready.requestFocusInWindow();
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