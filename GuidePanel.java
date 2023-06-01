import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuidePanel extends JPanel
{
   private BufferedImage img;  
   private Graphics gr;
   
   private Title title;
   private ImageIcon bg;

   private final int width = 880;
   private final int height = 720;
   
   private Timer t;
   private ArrayList<Animatable> animationObjects;
   
   private HoverImage back;
   private ArrayList<HoverImage> hovers;
   
   private TunnelsPanel owner;
   private boolean muteState, previousMuteState, isFocused;
   
   public GuidePanel(TunnelsPanel o)
   {
      owner = o;
      muteState = false;
      previousMuteState = false;
      isFocused = false;

      bg = new ImageIcon("img/title/TheGuide.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      
      hovers = new ArrayList<HoverImage>();
      
      back = new HoverImage(20,20, 100, 32, "img/title/Back.png", "img/title/BackHover.png");
      back.drawMe(gr);
      hovers.add(back);      
      
      
      setPreferredSize(new Dimension(width, height));
      
      animationObjects = new ArrayList<Animatable>();
      animationObjects.add(title);
      
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addMouseListener(new Mouse());
      addMouseMotionListener(new Mouse());
      
      addKeyListener(new Key());  
      setFocusable(true);
   
   }
   
   public void setFocus(boolean f)
   {
      isFocused = f;
   }
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(img, 0, 0, getWidth(), getHeight(), null);  
   }
   
   public void animate()
   {      
      
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      

      for (HoverImage hover : hovers)
      {
         hover.drawMe(gr);
      }      

      muteState = owner.getMute();
      if (previousMuteState != muteState)
      {
         // The player has pressed "M"
         if (muteState)
         {
            owner.StopMusic();
         }
         else
         {
            owner.StartMusic();
         }
      }
      previousMuteState = muteState;

      repaint();
   }
   
   private class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)  
      {
         if (isFocused)
         {
            animate();
         }
      }
   }
   
   private class Key extends KeyAdapter 
   {
      public void keyPressed(KeyEvent e) 
      {
         if(e.getKeyCode() == KeyEvent.VK_K)
         {
            owner.StopMusic();
         }
        
         
         if (e.getKeyCode() == KeyEvent.VK_L)
         {
            owner.StartMusic();
         }
         
      }
   }
       
   
   private class Mouse extends MouseAdapter
   {
      public void mouseClicked(MouseEvent me)
      {
         Point clicked = me.getPoint();
         
         Rectangle boundsBack = new Rectangle(back.getX(), back.getY(), back.getWidth(), back.getHeight());
         
         if (boundsBack.contains(clicked))
         {
            owner.goTitle();
         }
                  
      }
      
      public void mouseMoved(MouseEvent me)
      {
         Point clicked = me.getPoint();
         for (HoverImage hover : hovers)
         {
            Rectangle bounds = new Rectangle(hover.getX(), hover.getY(), hover.getWidth(), hover.getHeight());
            if (bounds.contains(clicked))
            {
               hover.switchHover();
            }
            else
            {
               hover.noHover();
            }
         }
      }
   } 
}