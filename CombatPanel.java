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
   
   private BufferedImage hitbox;
   private Graphics hitboxGr; 
   
   public CombatPanel(Enemy en, TunnelsPanel o)
   {
      owner = o;
      setPreferredSize(new Dimension(width, height));
      
      hitbox = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      hitboxGr = hitbox.getGraphics();
      hitboxGr.drawImage((new ImageIcon("maps/hitboxes/Combat.png")).getImage(), 0, 0, width, height, null);
      
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      animationObjects = new ArrayList<Animatable>();  
   
      c = new Character(this);
      animationObjects.add(c); 
      
      e = en;
      e.setWidth(150);
      e.setHeight(150);
      e.setX(140);
      e.setY(100);
      animationObjects.add(e);
      
      p = e.getProjectile();
      
      t = new Timer(5, new AnimationListener());
      t.start();
      
      addKeyListener(new Key());
      setFocusable(true);
   }
   
   public void collisions(Character c)
   {
      int w = c.getWidth();
      int h = c.getHeight();
      Color[][] map = Map.getArray(hitbox);
      
      //left collisions
      if (Map.colorDistance(map[c.getY() + h][c.getX() - 15], Color.BLACK) < 20)
      {
         c.setX(c.getX() + 15);
      }
      
      //right collisions    
      if (Map.colorDistance(map[c.getY() + h][c.getX() + w + 15], Color.BLACK) < 20)
      {
         c.setX(c.getX() - 15);
      }
      
      //top collisions 
      for (int i = c.getX() - 15; i < c.getX() + w + 15; i ++)
      {
      
         if (Map.colorDistance(map[c.getY() - 15][i], Color.BLACK) < 20)
         {
            c.setY(c.getY() + 15);
            break;
         }
      
      }
   
      //bottom collisions
      for (int i = c.getX() - 15; i < c.getX() + w + 15; i ++)
      {
         if (Map.colorDistance(map[c.getY() + h + 15][i], Color.BLACK) < 20)
         {
            c.setY(c.getY() - 15);
            break;
         }      
      } 
   }
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);  
   }
   
   
   public void animate()
   {      
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      
      myBuffer.drawImage((new ImageIcon("maps/display/Combat.png")).getImage(), 0, 0, width, height, null);
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(myBuffer);  
      }
      //System.out.println(e.getX() + " " + e.getY());
      myBuffer.drawImage((new ImageIcon("img/sprites/Skeleton1.png")).getImage(), 0, 0, 150, 150,  null);
      collisions(c);      
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
            
            c.setDX(c.getDX() - 15);
            left = true;  
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && !right)
         {
            
            c.setDX(c.getDX() + 15);
            right = true;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP && !up)
         {
            
            c.setDY(c.getDY() - 15);
            
            up = true;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN && !down)
         {
            
            c.setDY(c.getDY() + 15);
            
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
            c.setDX(c.getDX() + 15);  //Again: add 2, don't set to 0 precisely.  Explanation in the assignment.
            left = false;  //User is no longer holding the left key, so set this back to false.
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT)
         {
            c.setDX(c.getDX() - 15);
            right = false;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP)
         {
            c.setDY(c.getDY() + 15);
            up = false;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN)
         {
            c.setDY(c.getDY() - 15);
            down = false;
         }
         
      }
   }
}