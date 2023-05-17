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

   private final int width = 990;
   private final int height = 810;
   
   private Timer t;
   private ArrayList<Animatable> animationObjects;
   
   private HoverImage newgame;
   private HoverImage load;
   private HoverImage settings;
   private HoverImage credits;
   private ArrayList<HoverImage> hovers;
   
   private TunnelsPanel owner;
   
   public TitlePanel(TunnelsPanel o)
   {
      owner = o;
      
      bg = new ImageIcon("img/title/Background.png");
      img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB); 
      gr = img.getGraphics();
      gr.drawImage(bg.getImage(), 0, 0, width, height, null);
      
      hovers = new ArrayList<HoverImage>();
      
      newgame = new HoverImage(200, 400, 200, 45, "img/title/NewGame.png", "img/title/NewGameHover.png");
      newgame.drawMe(gr);
      hovers.add(newgame);      
      
      load = new HoverImage(600, 400, 200, 40, "img/title/Load.png", "img/title/LoadHover.png");
      load.drawMe(gr);
      hovers.add(load);
      
      settings = new HoverImage(200, 600, 200, 40, "img/title/Settings.png", "img/title/SettingsHover.png");
      settings.drawMe(gr);
      hovers.add(settings);      
      
      credits = new HoverImage(600, 600, 200, 40, "img/title/Credits.png", "img/title/CreditsHover.png");
      credits.drawMe(gr);
      hovers.add(credits);
      
      setPreferredSize(new Dimension(width, height));
      
      animationObjects = new ArrayList<Animatable>();
      title = new Title(this);
      animationObjects.add(title);
      
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
      
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(gr);  
      }
      for (HoverImage hover : hovers)
      {
         hover.drawMe(gr);
      }      
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
         for (HoverImage hover : hovers)
         {
            Rectangle bounds = new Rectangle(hover.getX(), hover.getY(), hover.getWidth(), hover.getHeight());
            if (bounds.contains(clicked))
            {
               owner.goWorld();
            }
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