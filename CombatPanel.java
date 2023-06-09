import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.*;

/**
* This class creates the space in which you fight enemies. 
*/
public class CombatPanel extends JPanel
{
   /**
   * Sets up buffered image
   */
   private BufferedImage myImage;  
   /**
   * Sets up graphics
   */        
   private Graphics myBuffer;
   /**
   * Constructor that initializes enemy in the combat panel
   */      
   private Enemy e;
   /**
   * Constructor that initializes projectiles which harm the player
   */   
   private Projectile p;
   /**
   * Constructor that initializes projectiles which harm the enemy
   */      
   private Projectile a;
   /**
   * Constructor that initializes the character in the combat panel
   */      
   private Character c;
   /**
   * Constructor that initializes TunnelsPanel as the owner of CombatPanel
   */   
   private TunnelsPanel owner;
   /**
   * Sets the width of the panel
   */      
   private final int width = 880;
   /**
   * Sets the height of the panel
   */
   private final int height = 720;
   /**
   * Sets up an array of the animation objects
   */   
   private ArrayList<Animatable> animationObjects;
   /**
   * Sets up a timer to wait for the transition
   */      
   private Timer transitionWait;
   /**
   * Sets up the main timer
   */      
   private Timer t;
   /**
   * Sets up a timer for the projectiles
   */   
   private Timer projectileTimer;
   /**
   * Sets up the timer for ammo
   */  
   private Timer ammoTimer;

   /**
   * Sets up an array list of projectiles and ammos
   */   
   private ArrayList<Projectile> projectiles, ammos;
   /**
   * Sets up a key listener
   */
   private KeyListener kl;
   private boolean left;
   private boolean right;
   private boolean up;
   private boolean down;
   /**
   * Makes the enemy "vibe" when hit
   */      
   private int enemyVibe = -1;
   /**
   * Makes the player "vibe" when hit
   */   
   private int vibe = -1;
   /**
   * Sets up a counter for when to start the text
   */
   private int startText = 300;
   /**
   * Checks to see whether music should currently be muted or not  
   */   
   private boolean muteState;
   /**
   * Checks the last mute state 
   */
   private boolean previousMuteState;
   /**
   * Checks to see which panel is in focus
   */
   private boolean isFocused; 
   /**
   * Audio clip for sound effect when player is hit
   */
   private Clip hit;
   /**
   * Audio clip for sound effect when enemy is hit
   */   
   private Clip enemyHit;
   
   /**
   * Sets up a buffered image for the map hitbox
   */
   private BufferedImage hitbox;
   /**
   * Sets up graphics for the hitbox
   */   
   private Graphics hitboxGr;
   /**
   * Sets up the combat panel with an enemy and tunnels panel as the owner
   */
   
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
      
      e = new Enemy(en.getName(), 0, 12, 250, 250, 0, en.getFrameOnePath(), en.getFrameTwoPath(), en.getProjectile(), en.getAmmo());
      e.setWidth((250 * en.getWidth()) / en.getHeight());
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

               startText = 0;
            
               transitionWait.stop();
            }
         });
      transitionWait.start();
      kl = new Key();
      addKeyListener(kl);
      setFocusable(true);
   }
   /**
   * Sets the focus to a panel
   */   
   public void setFocus(boolean f)
   {
      isFocused = f;
   }
    /**
    * Uses "hitboxes" with certain colors that the character can or cannot move through.
    */
   
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
   /**
   * Sets up collisions with projectiles
   */
    
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
   /**
   * Stops everything in the battle
   */   
   public void stopScreen()
   {
      removeKeyListener(kl);
      c.setDX(0);
      c.setDY(0);
      ammoTimer.stop();
      projectileTimer.stop();
      for (Projectile projectile : projectiles)
      {
         projectile.setX(1000);
         
         projectile.setDX(0);
      }
      for (Projectile ammo : ammos)
      {
         ammo.setX(1000);
         ammo.setDX(0);
      }
   }
   /**
   * Paints and repaints components
   */
   public void paintComponent(Graphics g)  
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
      g.setColor(Color.WHITE);
      g.setFont(new Font("Monospaced", Font.BOLD, 30)); 
      g.drawString("Your Health", 10, 40);
      g.drawString("Enemy Health", 600, 40);

      String enemyName = e.getName();
      if (startText < 50)
      {
         g.drawString( enemyName + " attacked you!", 70, 650);
         startText++;
      }

      else if (startText < 150 && enemyName.equals("A ghost"))
      {
         g.drawString("Dodge its harmful attacks!", 70, 650);
         startText++;
      }

      else if (startText < 250 && enemyName.equals("A ghost"))
      {
         g.drawString("Hit the red orbs to defeat it!", 70, 650);
         startText++;
      }

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
   /**
   * Ends the combat
   */   
   public void end(boolean result)
   {
      t.stop();

      // Pause for 1 seconds
      try
      {
         TimeUnit.SECONDS.sleep(1);
      }
      catch (InterruptedException ex) {}
                    
      owner.endCombat(result);
   }
   /**
   * Handles removing health when player collides with projectiles, sound effects, and winning/losing battle
   */   
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
            end(true);
         }
      }
      
      if (e.getHealth() == 0 || owner.world.ch.getHealth() == 0)
      {
         stopScreen();
      }
      repaint();
      
      
      
   }
   
   //private classes
   
   /**
   * Animates components
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
   * Handles the random spawning of projectiles
   */
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
         Projectile temp = new Projectile(p.getWidth(), p.getHeight(), p.getProjectilePath(), p.getRightProjectilePath(), p.getDamage(), p.getSpawnSpeed(), p.getMinSpeed(), p.getMaxSpeed());
         temp.setX(x);
         temp.setY(y);
         temp.setDX(dX);
         projectiles.add(temp);
      }
   }
   /**
   * Spwans the ammo 
   */
   
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
         Projectile temp = new Projectile(a.getWidth(), a.getHeight(), a.getProjectilePath(), a.getRightProjectilePath(), a.getDamage(), a.getSpawnSpeed(), a.getMinSpeed(), a.getMaxSpeed());
         temp.setX(x);
         temp.setY(y);
         temp.setDX(dX);
         ammos.add(temp);
      }
   }
   /**
   * Allows for key press
   */   
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
      }
   /**
   * Allows for key released events
   */
      
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