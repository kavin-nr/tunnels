import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class Map
{
   
   private ImageIcon imageSrc, hitboxSrc;
   
   private BufferedImage image, hitbox;
   
   private Graphics imageGr, hitboxGr;
   
   
   private Map prev=null, next=null;
   
   private int prevX, prevY, nextX, nextY;
   
   private WorldPanel owner;
   
   // enemy field 1
   // enemy fields 2
   
  
   public Map(String imageFilename, String hitboxFilename, int prevXV, int prevYV, int nextXV, int nextYV, WorldPanel o)
   {
      owner = o;
      imageSrc = new ImageIcon(imageFilename);
      hitboxSrc = new ImageIcon(hitboxFilename);
      
      image = new BufferedImage(owner.getWidth(), owner.getHeight(), BufferedImage.TYPE_INT_RGB);
      imageGr = image.getGraphics();
      imageGr.drawImage(imageSrc.getImage(), 0, 0, owner.getWidth(), owner.getHeight(), null);
      
      hitbox = new BufferedImage(owner.getWidth(), owner.getHeight(), BufferedImage.TYPE_INT_RGB);
      hitboxGr = hitbox.getGraphics();
      hitboxGr.drawImage(hitboxSrc.getImage(), 0, 0, owner.getWidth(), owner.getHeight(), null);
      
   
      prevX = prevXV;
      prevY = prevYV;
      nextX = nextXV;
      nextY = nextYV;
      
   }
   
   public Map getPrev()
   {
      return prev;
   }
   
   public Map getNext()
   {
      return next;
   }
   
   public int getPrevX()
   {
      return prevX;
   }
   
   public int getNextX()
   {
      return nextX;
   }
   
   public int getPrevY()
   {
      return prevY;
   }
   
   public int getNextY()
   {
      return nextY;
   }
   
   public void setPrev(Map prevv)
   {
      prev = prevv;
   }
   
   public void setNext(Map nextv)
   {
      next = nextv;
   }
   
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
   
   private static int colorDistance(Color one, Color two)
   {
      return (int) (Math.sqrt((Math.pow(two.getRed() - one.getRed(), 2))+(Math.pow(two.getGreen() - one.getGreen(), 2))+(Math.pow(two.getBlue() - one.getBlue(), 2))));
   }
   
   public void collisions()
   {
      int w = owner.ch.getWidth();
      int h = owner.ch.getHeight();
      Color[][] map = getArray(hitbox);
      
      //left collisions
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 2], Color.BLACK) < 20)
      {
         owner.ch.setX(owner.ch.getX() + 5);
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 2], Color.GREEN) < 20)
      {
         owner.goNext();
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 2], Color.RED) < 20)
      {
         owner.goPrev();
      }
      
      //right collisions    
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 1], Color.BLACK) < 20)
      {
         owner.ch.setX(owner.ch.getX() - 5);
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 1], Color.GREEN) < 20)
      {
         owner.goNext();
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 1], Color.RED) < 20)
      {
         owner.goPrev();
      }
   
      
      //top collisions 
      for (int i = owner.ch.getX() -2; i < owner.ch.getX() + w + 2; i ++)
      {
         
         if (colorDistance(map[owner.ch.getY() + h - 2][i], Color.BLACK) < 20)
         {
            owner.ch.setY(owner.ch.getY() + 5);
         
         }
         
         if (colorDistance(map[owner.ch.getY()-2][i], Color.GREEN) < 20)
         {
            owner.goNext();
         }
         
         if (colorDistance(map[owner.ch.getY()-2][i], Color.RED) < 20)
         {
            owner.goPrev();
         }
         
      }
      
      //bottom collisions
      for (int i = owner.ch.getX() - 2; i < owner.ch.getX() + w + 2; i ++)
      {
         if (colorDistance(map[owner.ch.getY() + h + 2][i], Color.BLACK) < 20)
         {
            owner.ch.setY(owner.ch.getY() - 5);;
         }
         
         if (colorDistance(map[owner.ch.getY() + h + 4][i], Color.GREEN) < 20)
         {
            owner.goNext();
         }
         
         if (colorDistance(map[owner.ch.getY() + h + 4][i], Color.RED) < 20)
         {
            owner.goPrev();
         }
         
      }
   } 
   // add enemy
   //collision
   //go back
   //go next
   public void drawMe(Graphics g)
   {
      g.drawImage(image, 0, 0, owner.getWidth(), owner.getHeight(), null);
   }
}