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
   
   private Timer t;

   private ArrayList<Animatable> animationObjects;
   
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
   
      myImage =  new BufferedImage(550, 450, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics(); 
      map1 = new Map("maps/display/Display1.png", "maps/hitboxes/Hitbox1.png", 275, 250, 200, 150, this);
      map2 = new Map("maps/display/Display2.png", "maps/hitboxes/Hitbox2.png", 200, 300, 470, 100, this);
      map1.setNext(map2);
      map2.setPrev(map1);
      map1.setPrev(map2);
      map2.setNext(map1);
      
      currentMap = map1;
      
      
      
      setPreferredSize(new Dimension(550, 450));
      
      animationObjects = new ArrayList<Animatable>();  
      
      ch = new Character(); 
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
   
   
   
   //instance methods
   
   
   public void animate()
   {      
      myImage =  new BufferedImage(550, 450, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics();
      currentMap.drawMe(myBuffer);
      
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(myBuffer);  
         boolean nul = currentMap.collisions();          
      }
      System.out.println(ch.getX());
      
      repaint();
   }
   
   
   public void goNext()
   {
      ch.setX(currentMap.getNext().getPrevX());
      ch.setY(currentMap.getNext().getPrevY());
      currentMap = currentMap.getNext();
   }
   
   public void goPrev()
   {
      ch.setX(currentMap.getPrev().getNextX());
      ch.setY(currentMap.getPrev().getNextY());
      currentMap = currentMap.getPrev();
      
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
            
            ch.setDX(ch.getDX() - 2);
            left = true;  
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && !right)
         {
            
            ch.setDX(ch.getDX() + 2);
            right = true;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP && !up)
         {
            
            ch.setDY(ch.getDY() - 2);
            
            up = true;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN && !down)
         {
            
            ch.setDY(ch.getDY() + 2);
            
            down = true;
         }
      }
      
      public void keyReleased(KeyEvent e) //Also overridden; ONE method that will be called any time a key is released
      {
         if(e.getKeyCode() == KeyEvent.VK_LEFT) // If the user lets go of the left arrow
         {
            ch.setDX(ch.getDX() + 2);  //Again: add 2, don't set to 0 precisely.  Explanation in the assignment.
            left = false;  //User is no longer holding the left key, so set this back to false.
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT)
         {
            ch.setDX(ch.getDX() -2);
            right = false;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP)
         {
            ch.setDY(ch.getDY() + 2);
            up = false;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN)
         {
            ch.setDY(ch.getDY() - 2);
            down = false;
         }
         
      }
   }
   
}
