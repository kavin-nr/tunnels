import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TunnelsPanel extends JPanel
{
   
   public TunnelsPanel()
   {
      setLayout(new BorderLayout());
      add(new WorldPanel());
      
      add(new DialoguePanel(), BorderLayout.SOUTH);
   }
}