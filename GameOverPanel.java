import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverPanel extends JPanel implements GamePanel
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
   
   public GameOverPanel(TunnelsPanel o)
   {
      owner = o;
      
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
               // if it is reload then do so and so
               // if it is quit then do so and so
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
               System.out.println("hover on");
               hover.switchHover();
            }
            else
            {
               System.out.println("hover off");
               hover.noHover();
            }
         }
      }
   } 
}