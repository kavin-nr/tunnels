import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverPanel extends JPanel
{
   private BufferedImage img;  
   private Graphics gr;
   
   private ImageIcon bg;

   private final int width = 880;
   private final int height = 720;
   
   private Timer t;
      
   private HoverImage reload;
   private HoverImage quit;
   
   private ArrayList<HoverImage> hovers;
   
   private TunnelsPanel owner;
   private boolean muteState, previousMuteState, isFocused;
   
   public GameOverPanel(TunnelsPanel o)
   {
      owner = o;
      muteState = false;
      previousMuteState = false;
      isFocused = false;
      
      bg = new ImageIcon("img/gameover/GameOver.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      
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
   
   private class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)  
      {
         animate();
      }
   }
   
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