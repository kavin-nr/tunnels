import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Arrays;

class WorldPanel extends JPanel
{
   //fields
   
   
   private BufferedImage myImage;  
   private Graphics myBuffer;
   
   
   private final int width = 990;
   private final int height = 810;
   
   
   private ArrayList<Animatable> animationObjects;
   private Timer t;
   
   private boolean left;
   private boolean right;
   private boolean up;
   private boolean down;
   
   private boolean leftCollide;
   private boolean rightCollide;
   private boolean upCollide;
   private boolean downCollide;
   
   public Character ch;
   
   private Map currentMap;
   private Map map1;
   private Map map2;
   
   //constructors
   public WorldPanel()
   {
      setPreferredSize(new Dimension(width, height));

      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics(); 
      map1 = new Map("maps/display/Display1.png", "maps/hitboxes/Hitbox1.png", 465, 350, 450, 250, this);
      map2 = new Map("maps/display/Display2.png", "maps/hitboxes/Hitbox2.png", 315, 500, 565, 100, this);
      map1.setNext(map2);
      map2.setPrev(map1);
      map1.setPrev(map2);
      map2.setNext(map1);
      
      Enemy Skeleton = new Enemy(450, 300, 100, 100, 15, "img/sprites/Skeleton1.png", "img/sprites/Skeleton2.png", null);
      map1.setEnemyOne(Skeleton);
      
      
      currentMap = map1;
      
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
         currentMap.collisions();          
      }      
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
            ch.setDX(ch.getDX() -5);
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
