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
   
   private Character ch;
   
   private ImageIcon map1 = new ImageIcon("map1.png");
   private BufferedImage myMap;
   private Graphics mapBuffer;
   
   
   //constructors
   public WorldPanel()
   {
   
      myImage =  new BufferedImage(550, 450, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics(); 
      myBuffer.drawImage(map1.getImage(), 0, 0, myImage.getWidth(), myImage.getHeight(), null);
      
      myMap = new BufferedImage(550, 450, BufferedImage.TYPE_INT_RGB);
      mapBuffer = myMap.getGraphics();
      mapBuffer.drawImage(map1.getImage(), 0, 0, myImage.getWidth(), myImage.getHeight(), null);
      
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
   public static Color[][] getArray(BufferedImage img)
   {
      Color[][] arr;
   	//
      int numcols = img.getWidth();
      int numrows = img.getHeight();
   	//
      arr = new Color[numrows][numcols];
   	//
      for(int j = 0; j < arr.length; j++)
      {
         for(int k = 0; k < arr[0].length; k++)
         {
            int rgb = img.getRGB(k,j);
         	//
            arr[j][k] = new Color(rgb);
         }
      }
   	//
      return arr;
   }
   
   public void animate()
   {      
      
      myBuffer.drawImage(map1.getImage(), 0, 0, myImage.getWidth(), myImage.getHeight(), null);
      
      
      for(Animatable animationObject : animationObjects)
      {
         animationObject.step();  
         animationObject.drawMe(myBuffer);  
         collisions();          
      }
      
      
      repaint();
   }
   
   private static int colorDistance(Color one, Color two)
   {
      return (int) (Math.sqrt((Math.pow(two.getRed() - one.getRed(), 2))+(Math.pow(two.getGreen() - one.getGreen(), 2))+(Math.pow(two.getBlue() - one.getBlue(), 2))));
   }
   
   public void collisions()
   {
      int w = ch.getWidth();
      int h = ch.getHeight();
      Color[][] map = getArray(myMap);
      
      //left collisions
      if (colorDistance(map[ch.getY() + h][ch.getX() - 2], Color.BLACK) < 20)
      {
         ch.setX(ch.getX() + 2);
      }
      
      //right collisions    
      if (colorDistance(map[ch.getY() + h][ch.getX() + w + 2], Color.BLACK) < 20)
      {
         ch.setX(ch.getX() - 2);
      }
      
      //top collisions 
      for (int i = ch.getX() -2; i < ch.getX() + w + 2; i ++)
      {
         
         if (colorDistance(map[ch.getY() - 2][i], Color.BLACK) < 20)
         {
            ch.setY(ch.getY() + 2);
         
         }
         
      }
      
      //bottom collisions
      for (int i = ch.getX() - 2; i < ch.getX() + w + 2; i ++)
      {
         if (colorDistance(map[ch.getY() + h + 2][i], Color.BLACK) < 20)
         {
            ch.setY(ch.getY() - 2);;
         }
         
      }
      
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
          
            ch.faceLeft();
            left = true;  
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && !right)
         {
            
            ch.setDX(ch.getDX() + 2);
            
            ch.faceRight();
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
