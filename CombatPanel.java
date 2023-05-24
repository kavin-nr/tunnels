import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.lang.Math;

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
   private Timer t2;
   
   private ArrayList<Projectile> projectiles;
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
      projectiles = new ArrayList<Projectile>();
      
      c = new Character(this);
      animationObjects.add(c); 
      
      e = new Enemy(0, 12, 250, 250, 0, en.getFrameOnePath(), en.getFrameTwoPath(), en.getProjectile());
      e.setX((width / 2 ) - (e.getWidth() / 2));
      animationObjects.add(e);
      
      p = e.getProjectile();
      
      t = new Timer(5, new AnimationListener());
      t.start();
      
      t2 = new Timer(p.getSpawnSpeed(), new ProjectileSpawner());
      t2.start();
      
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
   
   public void projectileCollisions(Projectile pr, int index)
   {
      boolean xOverlap = false;
      boolean yOverlap = false;
      if ((c.getX() < pr.getX() && pr.getX() < c.getX() + c.getWidth()) || (c.getX()  < pr.getX() + pr.getWidth() && pr.getX() + pr.getWidth() < c.getX() + c.getWidth()))
      {
         xOverlap = true;
      }
      if ((c.getY() < pr.getY() && pr.getY() < c.getY() + c.getHeight()) || (c.getY() < pr.getY() + pr.getHeight() && pr.getY() + pr.getHeight() < c.getY() + c.getHeight()))
      {
         yOverlap = true;
      }
      
      if (xOverlap && yOverlap)
      {        
         // Subtract projectile damage from player health 
         owner.world.ch.setHealth(owner.world.ch.getHealth() - pr.getDamage());
         // Essentially delete the projectile since it is sending it outside of bounds, and any projectiles outside of bounds will not be animated
         pr.setX(1000);
      }
   }
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
      g.setColor(Color.WHITE);
      g.setFont(new Font("Monospaced", Font.BOLD, 30)); 
      g.drawString("Your Health", 10, 40);
      int health = owner.world.ch.getHealth();
      if (health <= 30)
      {
         g.setColor(new Color(214, 19, 11));
      }
      else if (health <= 65)
      {
         g.setColor(new Color(186, 172, 41));
      }
      else 
      {
         g.setColor(Color.GREEN);
      }
      g.drawString("" + health, 10, 80);
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
      
      int toRemoveBound = -1;
      int toRemoveCollide = -1;
      int index = 0;
      for (Projectile projectile : projectiles)
      {
         projectile.step();
         projectileCollisions(projectile, index);
         
         // Logs the index of projectiles that are outside of box boundaries
         if (projectile.getX() > 770 || projectile.getX() < 60)
         {
            toRemoveBound = index;
         }
         projectile.drawMe(myBuffer);
         index++;
      }
      
      // Deletes projectiles that were found to be outside earlier
      if (toRemoveBound != -1)
      {
         projectiles.remove(toRemoveBound);
      }
      
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
   
   private class ProjectileSpawner implements ActionListener
   {
      int count = 0;
      int x;
      int dX;
      int y;
      public void actionPerformed(ActionEvent e)  
      {
         dX = (int) ((Math.random() * (p.getMaxSpeed() - p.getMinSpeed())) + p.getMinSpeed());
         if (count % 2 == 0)
         {
            x = 65;
         }
         else
         {
            x = 770;
            dX = -dX;
         } 
         count++;
         
         y = (int) (285 + (Math.random() * 350));
         Projectile temp = new Projectile(x, y, p.getWidth(), p.getHeight(), p.getProjectilePath(), p.getDamage(), p.getSpawnSpeed(), p.getMinSpeed(), p.getMaxSpeed());
         temp.setDX(dX);
         projectiles.add(temp);
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