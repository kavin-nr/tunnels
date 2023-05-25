import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Arrays;

public class WorldPanel extends JPanel
{
   //fields
   
   
   private BufferedImage myImage;  
   private Graphics myBuffer;
   
   
   private final int width = 880;
   private final int height = 720;
   
   
   private ArrayList<Animatable> animationObjects;
   private Timer t;
   
   private boolean left;
   private boolean right;
   private boolean up;
   private boolean down;
   
   public Character ch;
   
   private Map currentMap;
   private Map map1;
   private Map map2;
   private Map map3;
   
   private TunnelsPanel owner;
   
   //constructors
   public WorldPanel(TunnelsPanel o)
   {
      owner = o;
      
      setPreferredSize(new Dimension(width, height));

      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics(); 
      map1 = new Map("maps/display/Display1.png", "maps/hitboxes/Hitbox1.png", 465, 350, 450, 250, this);
      map2 = new Map("maps/display/Display2.png", "maps/hitboxes/Hitbox2.png", 315, 400, 565, 100, this);
      map3 = new Map("maps/display/Display3.png", "maps/hitboxes/Hitbox3.png", 180, 515, 420, 205, this);
      
      map1.setNext(map2);
      map2.setPrev(map1);
      map2.setNext(map3);
      map3.setPrev(map2);
      
      Projectile strongAmmo = new Projectile(20, 20, "img/proj/Ammo.png", 50, 5000, 4, 6);
      Projectile bone = new Projectile(50, 16, "img/proj/Bone.png", 10, 500, 7, 10);
      Projectile zomb = new Projectile(50, 16, "img/proj/Knife.png", 5, 200, 3, 4);
      Projectile ghoost = new Projectile(25, 25, "img/proj/Ghoost.png", 10, 500, 5, 8);
            
      Savepoint three = new Savepoint(400, 280, 65, 65, "img/sprites/Save.png");
      Enemy Ghost1 = new Enemy(250, 225, 100, 100, 30, "img/sprites/Spirit1L.png", "img/sprites/Spirit2L.png", ghoost, strongAmmo);
      Enemy Ghost2 = new Enemy(550, 185, 100, 100, 30, "img/sprites/Spirit1.png", "img/sprites/Spirit2.png", ghoost, strongAmmo);
      map2.addEnemy(Ghost1);
      map2.addEnemy(Ghost2);
      map3.setSavepoint(three);
      
      currentMap = map3;
      
      animationObjects = new ArrayList<Animatable>();  
      
      ch = new Character(this); 
      animationObjects.add(ch); 
      
      t = new Timer(5, new AnimationListener());
      t.start();  
            
      addKeyListener(new Key());  
      setFocusable(true);  
      
   }
   
   
   //overridden methods
   
   public void paintComponent(Graphics g)  
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);  
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   
   
   //instance methods
   
   
   public void animate()
   {      
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      currentMap.drawMe(myBuffer);
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(myBuffer);  
      }    
      currentMap.collisions();  
      repaint();
   }
   
   public void goNext()
   {
      ch.setX(currentMap.getNext().getPrevX());
      ch.setY(currentMap.getNext().getPrevY());
      currentMap = currentMap.getNext();      
      System.out.println("next");
   }
   
   public void goPrev()
   {
      ch.setX(currentMap.getPrev().getNextX());
      ch.setY(currentMap.getPrev().getNextY());
      currentMap = currentMap.getPrev();
      System.out.println("prev");
   }
   
   public void goCombat(Enemy e)
   
   {
      left = false;
      right = false;
      up = false;
      down = false;
      ch.setDX(0);
      ch.setDY(0);
      owner.goCombat(e);
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
            
            ch.setDX(ch.getDX() - 5);
            left = true;  
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && !right)
         {
            
            ch.setDX(ch.getDX() + 5);
            right = true;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP && !up)
         {
            
            ch.setDY(ch.getDY() - 5);
            
            up = true;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN && !down)
         {
            
            ch.setDY(ch.getDY() + 5);
            
            down = true;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_SPACE)
         {
            
            System.out.println(ch.getX() + " " + ch.getY());
         }
      }
      
      public void keyReleased(KeyEvent e) //Also overridden; ONE method that will be called any time a key is released
      {
         if(e.getKeyCode() == KeyEvent.VK_LEFT) // If the user lets go of the left arrow
         {
            ch.setDX(ch.getDX() + 5);  //Again: add 2, don't set to 0 precisely.  Explanation in the assignment.
            left = false;  //User is no longer holding the left key, so set this back to false.
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT)
         {
            ch.setDX(ch.getDX() - 5);
            right = false;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP)
         {
            ch.setDY(ch.getDY() + 5);
            up = false;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN)
         {
            ch.setDY(ch.getDY() - 5);
            down = false;
         }
         
      }
   }
   
}
