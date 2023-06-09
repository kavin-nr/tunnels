import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
* Class that displays the screen shown when you die. 
*/

public class GameOverPanel extends JPanel
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
   * Sets up the background image
   */   
   
   private ImageIcon bg;
   /**
   * Sets the width of the panel
   */

   private final int width = 880;
   /**
   * Sets the height of the panel
   */   
   
   private final int height = 720;
   /**
   * Sets up the main timer
   */   
   
   private Timer t;
   /**
   *Sets up the hover image for the reload button
   */
      
   private HoverImage reload;
   /**
   * Sets up the hover image for the quit button
   */   
   
   private HoverImage quit;
   /**
   * Initializes the array list of hover images
   */   
   
   private ArrayList<HoverImage> hovers;
   
   /**
   * Sets up the tunnels panel as the owner.
   */   
   private TunnelsPanel owner;
   
   /**
   * Constructors for sound.
   */
   
   private boolean muteState, previousMuteState, isFocused;
   
   /**
   * Sets up the game over panel
   */   
   
   public GameOverPanel(TunnelsPanel o)
   {
      owner = o;
      muteState = false;
      previousMuteState = false;
      isFocused = false;
      //Sets the background to the game over screen
      bg = new ImageIcon("img/gameover/GameOver.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      //Sets up the buttons that implement hovering
      hovers = new ArrayList<HoverImage>();
      
      reload = new HoverImage(200, 450, 160, 60, "img/gameover/Reload.png", "img/gameover/ReloadHover.png");
      reload.drawMe(gr);
      hovers.add(reload);      
      
      quit = new HoverImage(510, 440, 160, 70, "img/gameover/Quit.png", "img/gameover/QuitHover.png");
      quit.drawMe(gr);
      hovers.add(quit);
      
      setPreferredSize(new Dimension(width, height));
    
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addMouseListener(new Mouse());
      addMouseMotionListener(new Mouse());
   }
   /**
   * Sets the focus to the panel
   */   
   
   public void setFocus(boolean f)
   {
      isFocused = f;
   }
   /**
   * Paints the components
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
            owner.stopDeath();
         }
         else
         {
            owner.startDeath();
         }
      }
      previousMuteState = muteState;

      repaint();
   }
   /**
   * Sets up animation
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
   * Sends the player back to the world or quits the game based on which button they press
   */
   
     
   private class Mouse extends MouseAdapter
   {
      public void mouseClicked(MouseEvent me)
      {
         Point clicked = me.getPoint();
         
         Rectangle boundsReload = new Rectangle(reload.getX(), reload.getY(), reload.getWidth(), reload.getHeight());
         Rectangle boundsQuit = new Rectangle(quit.getX(), quit.getY(), quit.getWidth(), quit.getHeight());
         if (boundsReload.contains(clicked))
         {
            owner.loadWorld();
         }
         if (boundsQuit.contains(clicked))
         {
            System.exit(0);
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