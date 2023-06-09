import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
* Class that displays the win screen
*/
public class WinPanel extends JPanel
{
   /**
   * Initializes buffered image
   */
   private BufferedImage img;  
   /**
   * Initializes graphics
   */   
   private Graphics gr;
   /**
   * Initializes a title
   */   
   private Title title;
   /**
   * Initializes an image icon for the background
   */
   private ImageIcon bg;
   /**
   * Sets panel width
   */
   private final int width = 880;
   /**
   * Sets panel height 
   */   
   private final int height = 720;
   /**
   * Initializes main timer
   */   
   private Timer t;
   /**
   * Sets up an array list of animation objects
   */   
   private ArrayList<Animatable> animationObjects;
   /**
   * Initializes a hover image for the finish button
   */   
   private HoverImage finish;
   /**
   * Sets up an array list of hover images
   */   
   private ArrayList<HoverImage> hovers;
   /**
   * Initializes tunnels panel as the owner
   */   
   private TunnelsPanel owner;
   /**
   * Constructors for sound
   */   
   private boolean muteState, previousMuteState, isFocused;
   /**
   * Stores time it took to finish game
   */   
   private int finishTime;
   /**
   * Initializes a win panel
   */      
   public WinPanel(TunnelsPanel o)
   {
      owner = o;
      muteState = false;
      previousMuteState = false;
      isFocused = false;

      bg = new ImageIcon("img/title/Win.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      
      hovers = new ArrayList<HoverImage>();
      
      finish = new HoverImage(350,600, 210, 75, "img/title/Finish.png", "img/title/FinishHover.png");
      finish.drawMe(gr);
      hovers.add(finish);      
      
      
      setPreferredSize(new Dimension(width, height));
      
      animationObjects = new ArrayList<Animatable>();
      animationObjects.add(title);
      
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addMouseListener(new Mouse());
      addMouseMotionListener(new Mouse());
      
   
   }
   
   public void setFinishTime(int playtime)
   {
      finishTime = playtime;
   }
   /**
   * Sets the focus to the panel
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
      g.setColor(Color.WHITE);
      g.setFont(new Font("Monospaced", Font.BOLD, 30));
      g.drawString("You were transported back home safely.", 100, 300);  
      g.drawString("The tunnels are now safe for colonization.", 50, 340);
      g.drawString("Time: " + finishTime / 60 + " minutes, " + finishTime % 60 + " seconds", 200, 450); 
   
      
        
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
   * Initializes an animation listener
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
   * Allows for mouse clicks
   */      
   private class Mouse extends MouseAdapter
   {
      public void mouseClicked(MouseEvent me)
      {
         Point clicked = me.getPoint();
         
         Rectangle boundsFinish = new Rectangle(finish.getX(), finish.getY(), finish.getWidth(), finish.getHeight());
         
         if (boundsFinish.contains(clicked))
         {
            owner.goTitle();
            owner.stopWin();
            owner.StartMusic();
         }
                  
      }
   /**
   * Allows for mouse detection
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