import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TunnelsPanel extends JPanel
{
   private TitlePanel title;
   private JFrame owner;
   private DialoguePanel dialogue;
   private WorldPanel world;
   
   public TunnelsPanel(JFrame o)
   {
      owner = o;
      setLayout(new BorderLayout());
      title = new TitlePanel(this);
      add(title);
      
      dialogue = new DialoguePanel();
      
      world = new WorldPanel();
   }
   
   public void goWorld()
   {
      remove(title);
      add(world);
      repaint();
      revalidate();
      owner.pack();
      world.requestFocusInWindow();
   }
}