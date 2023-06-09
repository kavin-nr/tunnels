import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
* Class that shows the screen with instructions in the title
*/

public class GuidePanel extends JPanel
{
   /**
   * Sets up a buffered image
   */

   private BufferedImage img;  
   /**
   * Sets up graphics
   */
   
   private Graphics gr;
   /**
   * Sets up the title
   */
   
   private Title title;
   /**
   * Sets up the background
   */
   
   private ImageIcon bg;
   /**
   * Sets up the width of the panel
   */

   private final int width = 880;
   /**
   * Sets up the height of the panel
   */
   
   private final int height = 720;
   /**
   * Sets up the main timer
   */

   private Timer t;
   /**
   * Sets up the array list of the animation objects
   */
   
   private ArrayList<Animatable> animationObjects;
   /**
   * Initializes back hover image
   */
   
   private HoverImage back;
   /**
   * Sets up an array list of hover images
   */
   
   private ArrayList<HoverImage> hovers;
   /**
   * Sets up tunnels panel as the owner panel
   */
   
   private TunnelsPanel owner;
   /**
   * Constructors for sound
   */
   
   private boolean muteState, previousMuteState, isFocused;
   /**
   * Sets up the guide panel
   */
   
   public GuidePanel(TunnelsPanel o)
   {
      owner = o;
      muteState = false;
      previousMuteState = false;
      isFocused = false;
      //Sets the background to the guide
      bg = new ImageIcon("img/title/TheGuide.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      //Sets up the button that takes you back to the title
      hovers = new ArrayList<HoverImage>();
      
      back = new HoverImage(20,20, 100, 32, "img/title/Back.png", "img/title/BackHover.png");
      back.drawMe(gr);
      hovers.add(back);      
      
      
      setPreferredSize(new Dimension(width, height));
      
      animationObjects = new ArrayList<Animatable>();
      animationObjects.add(title);
      //Sets up timer as well as mouse and key input
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addMouseListener(new Mouse());
      addMouseMotionListener(new Mouse());
      
      addKeyListener(new Key());  
      setFocusable(true);
   
   }
   /**
   * Sets the focus to to the panel
   */
   
   public void setFocus(boolean f)
   {
      isFocused = f;
   }
   /**
   * Paints the graphics components
   */
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(img, 0, 0, getWidth(), getHeight(), null);  
   }
   /**
   * Animates components
   */
   
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
   /**
   * Animates components with an action listener
   */
   
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
   /**
   * Key detection
   */
   
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
       
   /**
   * Mouse detections
   */
  
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
   /**
   * Mouse detections
   */
      
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