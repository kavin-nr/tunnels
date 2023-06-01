import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.*;


public class CombatPanel extends JPanel
{

   private BufferedImage myImage;  
   private Graphics myBuffer;
   
   private Enemy e;
   // projectiles that hurt player
   private Projectile p;
   // projectiles that hurt enemy
   private Projectile a;
   private Character c;
   private TunnelsPanel owner;
   
   private final int width = 880;
   private final int height = 720;
   
   private ArrayList<Animatable> animationObjects;
   private Timer transitionWait;
   private Timer t;
   private Timer projectileTimer;
   private Timer ammoTimer;
   
   private ArrayList<Projectile> projectiles, ammos;
   
   private boolean left;
   private boolean right;
   private boolean up;
   private boolean down;
   
   private int enemyVibe = -1;
   private int vibe = -1;
   
   private boolean muteState;
   private boolean previousMuteState;
   private boolean isFocused; 
   private Clip hit;
   private Clip enemyHit;
   
   
   private BufferedImage hitbox;
   private Graphics hitboxGr;

   
   public CombatPanel(Enemy en, TunnelsPanel o)
   {
      owner = o;
      
      isFocused = false;
   
      muteState = false;
      previousMuteState = false;
      
      setPreferredSize(new Dimension(width, height));
      
      hitbox = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      hitboxGr = hitbox.getGraphics();
      hitboxGr.drawImage((new ImageIcon("maps/hitboxes/Combat.png")).getImage(), 0, 0, width, height, null);
      
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      
      animationObjects = new ArrayList<Animatable>();  
      projectiles = new ArrayList<Projectile>();
      ammos = new ArrayList<Projectile>();
      
      c = new Character(this);
      animationObjects.add(c); 
      
      e = new Enemy(0, 12, 250, 250, 0, en.getFrameOnePath(), en.getFrameTwoPath(), en.getProjectile(), en.getAmmo());
      e.setX((width / 2 ) - (e.getWidth() / 2));
      animationObjects.add(e);
      
      p = e.getProjectile();
      a = e.getAmmo();
      
      t = new Timer(5, new AnimationListener());
      t.start();
   
      // Since the panel is initialized before the transition, sometimes the player might not be ready for a sudden attack
      // Wait for two seconds before actually shooting projectiles
      transitionWait = new Timer(2000, 
         new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               projectileTimer = new Timer(p.getSpawnSpeed(), new ProjectileSpawner());
               projectileTimer.start();
            
               ammoTimer = new Timer(a.getSpawnSpeed(), new AmmoSpawner());
               ammoTimer.start();
            
            
               transitionWait.stop();
            }
         });
      transitionWait.start();
      addKeyListener(new Key());
      setFocusable(true);
   }
   
   public void setFocus(boolean f)
   {
      isFocused = f;
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
   
   public boolean projectileCollisions(Projectile pr)
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
         // Essentially delete the projectile since it is sending it outside of bounds, and any projectiles outside of bounds will not be animated
         pr.setX(1000);
         // Tell whoever called the function that a projectile has collided
         return true;
      }
      return false;
   }
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
      g.setColor(Color.WHITE);
      g.setFont(new Font("Monospaced", Font.BOLD, 30)); 
      g.drawString("Your Health", 10, 40);
      g.drawString("Enemy Health", 600, 40);
      int health = owner.world.ch.getHealth();
      int enemyHealth = e.getHealth();
      
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
      
      if (enemyHealth <= 30)
      {
         g.setColor(new Color(214, 19, 11));
      }
      else if (enemyHealth <= 65)
      {
         g.setColor(new Color(186, 172, 41));
      }
      else 
      {
         g.setColor(Color.GREEN);
      }
      g.drawString("" + enemyHealth, 600, 80);
   }
   
   public void end(boolean result)
   {
      t.stop();
      ammoTimer.stop();
      projectileTimer.stop();
         
      // Pause for 1 seconds
      try
      {
         TimeUnit.SECONDS.sleep(1);
      }
      catch (InterruptedException ex) {}
                    
      owner.endCombat(result);
   }
   
   public void animate()
   {      
      muteState = owner.getMute();
   
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      
      myBuffer.drawImage((new ImageIcon("maps/display/Combat.png")).getImage(), 0, 0, width, height, null);
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(myBuffer);  
      }
      
      int toRemove = -1;
      int index = 0;
      for (Projectile projectile : projectiles)
      {
         projectile.step();
         // If a projectile has collided with player, subtract projectile damage from player health
         if (projectileCollisions(projectile))
         {
            if (!muteState)
            {
               hit = owner.openMusic("music/hit.wav");
               hit.start();
            }
         
            if (projectile.getDamage() < owner.world.ch.getHealth())
            {
               owner.world.ch.setHealth(owner.world.ch.getHealth() - projectile.getDamage());
            }
            else
            { 
               owner.world.ch.setHealth(0);
            }
            
            vibe = 0;
         }
         // Logs the index of projectiles that are outside of box boundaries
         if (projectile.getX() > 800 || projectile.getX() < 40)
         {
            toRemove = index;
         }
         projectile.drawMe(myBuffer);
         index++;
      }
      
      // Deletes projectiles that were found to be outside earlier
      if (toRemove != -1)
      {
         projectiles.remove(toRemove);
      }
      
      toRemove = -1;
      index = 0;
      for (Projectile ammo : ammos)
      {
         ammo.step();
         
         // If a projectile has collided with player, subtract projectile damage from player health
         if (projectileCollisions(ammo))
         {
            if (!muteState)
            {
               enemyHit = owner.openMusic("music/enemy_hit.wav");
               enemyHit.start();
            }
            
         
            if (ammo.getDamage() < e.getHealth())
            {
               e.setHealth(e.getHealth() - ammo.getDamage());
            }
            else
            {
               e.setHealth(0);
            }
            
            enemyVibe = 0;
         }
         // Logs the index of ammos that are outside of box boundaries
         if (ammo.getX() > 800 || ammo.getX() < 40)
         {
            toRemove = index;
         }
         ammo.drawMe(myBuffer);
         index++;
      }
      
      // Deletes ammos that were found to be outside earlier
      if (toRemove != -1)
      {
         ammos.remove(toRemove);
      }
      
      collisions(c);
      
      muteState = owner.getMute();
      if (previousMuteState != muteState)
      {
         // The player has pressed "M"
         if (muteState)
         {
            owner.StopMusic2();
         }
         else
         {
            owner.StartMusic2();
         }
      }
      previousMuteState = muteState;
      
      if (vibe <= 10)
      {
         if (vibe % 2 == 0)
         {
            c.setX(c.getX() + 3);
         }
         else
         {
            c.setX(c.getX() - 3);
         }
         vibe++;
      }
      else
      {
         if (owner.world.ch.getHealth() == 0)
         {
            // Player died
            end(false);
         }
      }
      
      if (enemyVibe <= 25)
      {
         if (enemyVibe % 2 == 0)
         {
            e.setX(e.getX() + 10);
         }
         else
         {
            e.setX(e.getX() - 10);
         }
         enemyVibe++;
      }
      else
      {
         e.setX((width / 2 ) - (e.getWidth() / 2));
         e.setY(12);
         if (e.getHealth() == 0)
         {
         // Battle was successful
            e.setX(1000);
            end(true);
         }
      }
               
      repaint();
      
      
      
   }
   
   //private classes
   
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
            x = 40;
         }
         else
         {
            x = 800;
            dX = -dX;
         } 
         count++;
         
         y = (int) (285 + (Math.random() * 350));
         Projectile temp = new Projectile(p.getWidth(), p.getHeight(), p.getProjectilePath(), p.getDamage(), p.getSpawnSpeed(), p.getMinSpeed(), p.getMaxSpeed());
         temp.setX(x);
         temp.setY(y);
         temp.setDX(dX);
         projectiles.add(temp);
      }
   }
   
   private class AmmoSpawner implements ActionListener
   {
      int count = 0;
      int x;
      int dX;
      int y;
      public void actionPerformed(ActionEvent e)  
      {
         dX = (int) ((Math.random() * (a.getMaxSpeed() - a.getMinSpeed())) + a.getMinSpeed());
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
         Projectile temp = new Projectile(a.getWidth(), a.getHeight(), a.getProjectilePath(), a.getDamage(), a.getSpawnSpeed(), a.getMinSpeed(), a.getMaxSpeed());
         temp.setX(x);
         temp.setY(y);
         temp.setDX(dX);
         ammos.add(temp);
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
         if(e.getKeyCode() == KeyEvent.VK_LEFT && left) // If the user lets go of the left arrow
         {
            c.setDX(c.getDX() + 15);  //Again: add 2, don't set to 0 precisely.  Explanation in the assignment.
            left = false;  //User is no longer holding the left key, so set this back to false.
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && right)
         {
            c.setDX(c.getDX() - 15);
            right = false;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP && up)
         {
            c.setDY(c.getDY() + 15);
            up = false;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN && down)
         {
            c.setDY(c.getDY() - 15);
            down = false;
         }
         
      }
   }
}