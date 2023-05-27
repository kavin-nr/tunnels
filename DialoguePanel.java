import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class DialoguePanel extends JPanel
{
   
   public DialoguePanel()
   {
      setLayout(new FlowLayout());
      setPreferredSize(new Dimension(200, 450));
      add(new JLabel("Hello!!"));      
      add(new JLabel("Hello!!"));  
      add(new JLabel("Hello!!"));  
   }
}