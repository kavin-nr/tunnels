import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class CombatPanel extends JPanel
{

   private BufferedImage myImage;  
   private Graphics myBuffer;
   
   private Enemy e;
   private Projectile p;
   private Character c;
   private TunnelsPanel owner;
   
   private final int width = 880;
   private final int height = 720;
   
   private ArrayList<Animatable> animationObjects;
   private Timer t;
   
   private boolean left;
   private boolean right;
   private boolean up;
   private boolean down;
   
   public CombatPanel(Enemy en, TunnelsPanel o)
   {
      owner = o;
      setPreferredSize(new Dimension(width, height));
      
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      animationObjects = new ArrayList<Animatable>();  
   
      c = new Character(this);
      animationObjects.add(c); 
      
      e = en;
      p = e.getProjectile();
      
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addKeyListener(new Key());
      setFocusable(true);
   }
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);  
   }
   
   public void animate()
   {      
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(myBuffer);  
      }      
      repaint();
   }
   
   //private classes
   
   private class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)  
      {
         animate();
      }
   }
   
   private class Key extends KeyAdapter 
   {
      public void keyPressed(KeyEvent e) 
      {
         if(e.getKeyCode() == KeyEvent.VK_LEFT && !left)
         {
            
            c.setDX(c.getDX() - 5);
            left = true;  
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && !right)
         {
            
            c.setDX(c.getDX() + 5);
            right = true;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP && !up)
         {
            
            c.setDY(c.getDY() - 5);
            
            up = true;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN && !down)
         {
            
            c.setDY(c.getDY() + 5);
            
            down = true;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_SPACE)
         {
            
            System.out.println(c.getX() + " " + c.getY());
         }
      }
      
      public void keyReleased(KeyEvent e) //Also overridden; ONE method that will be called any time a key is released
      {
         if(e.getKeyCode() == KeyEvent.VK_LEFT) // If the user lets go of the left arrow
         {
            c.setDX(c.getDX() + 5);  //Again: add 2, don't set to 0 precisely.  Explanation in the assignment.
            left = false;  //User is no longer holding the left key, so set this back to false.
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT)
         {
            c.setDX(c.getDX() - 5);
            right = false;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP)
         {
            c.setDY(c.getDY() + 5);
            up = false;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN)
         {
            c.setDY(c.getDY() - 5);
            down = false;
         }
         
      }
   }
}