import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TitlePanel extends JPanel
{
   private BufferedImage img;  
   private Graphics gr;
   
   private Title title;
   private ImageIcon bg;

   private final int width = 880;
   private final int height = 720;
   
   private Timer t;
   private ArrayList<Animatable> animationObjects;
   
   private HoverImage newgame;
   private HoverImage load;
   private HoverImage guide;
   private HoverImage quit;
   private ArrayList<HoverImage> hovers;
   
   private TunnelsPanel owner;
   private boolean muteState, previousMuteState, isFocused;
   
   public TitlePanel(TunnelsPanel o)
   {
      owner = o;
      muteState = false;
      previousMuteState = false;
      isFocused = false;

      bg = new ImageIcon("img/title/Background.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      
      hovers = new ArrayList<HoverImage>();
      
      newgame = new HoverImage(150, 400, 200, 45, "img/title/NewGame.png", "img/title/NewGameHover.png");
      newgame.drawMe(gr);
      hovers.add(newgame);      
      
      load = new HoverImage(550, 400, 200, 40, "img/title/Load.png", "img/title/LoadHover.png");
      load.drawMe(gr);
      hovers.add(load);
      
      guide = new HoverImage(150, 600, 200, 40, "img/title/Guide.png", "img/title/GuideHover.png");
      guide.drawMe(gr);
      hovers.add(guide);      
      
      quit = new HoverImage(550, 600, 200, 40, "img/title/Quit.png", "img/title/QuitHover.png");
      quit.drawMe(gr);
      hovers.add(quit);
      
      setPreferredSize(new Dimension(width, height));
      
      animationObjects = new ArrayList<Animatable>();
      title = new Title(this);
      animationObjects.add(title);
      
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addMouseListener(new Mouse());
      addMouseMotionListener(new Mouse());
      
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
      
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(gr);  
      }
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
   
   
   private class Mouse extends MouseAdapter
   {
      public void mouseClicked(MouseEvent me)
      {
         Point clicked = me.getPoint();
         
         Rectangle boundsNew = new Rectangle(newgame.getX(), newgame.getY(), newgame.getWidth(), newgame.getHeight());
         Rectangle boundsLoad = new Rectangle(load.getX(), load.getY(), load.getWidth(), load.getHeight());
         Rectangle boundsGuide = new Rectangle(guide.getX(), guide.getY(), guide.getWidth(), guide.getHeight());
         Rectangle boundsQuit = new Rectangle(quit.getX(), quit.getY(), quit.getWidth(), quit.getHeight());
         
         if (boundsNew.contains(clicked))
         {
            owner.newWorld();
         }

         else if (boundsLoad.contains(clicked))
         {
            owner.loadWorld();
         }

         else if (boundsGuide.contains(clicked))
         {
            owner.goGuide();
         }
      
         else if (boundsQuit.contains(clicked))
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