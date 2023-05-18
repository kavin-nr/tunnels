import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Map
{
   
   private ImageIcon imageSrc, hitboxSrc;
   
   private BufferedImage image, hitbox;
   
   private Graphics imageGr, hitboxGr;
   
   
   private Map prev, next;
   
   private int prevX, prevY, nextX, nextY;
   
   private WorldPanel owner;
   
   private Enemy enemyOne = null;
   private Enemy enemyTwo = null;
   private ArrayList<Enemy> enemies;

  
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
      
      enemies = new ArrayList<Enemy>();
      
      
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
   
   public Enemy getEnemyOne()
   {
      return enemyOne;
   }
   
   public Enemy getEnemyTwo()
   {
      return enemyTwo;
   }
   
   public void setPrev(Map prevv)
   {
      prev = prevv;
   }
   
   public void setNext(Map nextv)
   {
      next = nextv;
   }
   
   public void setEnemyOne(Enemy oneVal)
   {
      enemyOne = oneVal;
      enemies.add(enemyOne);
   }
   
   public void setEnemyTwo(Enemy twoVal)
   {
      enemyTwo = twoVal;
      enemies.add(enemyTwo);
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
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 1], Color.BLACK) < 20)
      {
         owner.ch.setX(owner.ch.getX() + 5);
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 1], Color.GREEN) < 20)
      {
         owner.goNext();
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 1], Color.RED) < 20)
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
      for (int i = owner.ch.getX() -1; i < owner.ch.getX() + w + 1; i ++)
      {
         
         if (colorDistance(map[owner.ch.getY() + h - 5][i], Color.BLACK) < 20)
         {
            owner.ch.setY(owner.ch.getY() + 5);
         
         }
         
         if (colorDistance(map[owner.ch.getY() - 5][i], Color.GREEN) < 20)
         {
            owner.goNext();
         }
         
         if (colorDistance(map[owner.ch.getY() - 5][i], Color.RED) < 20)
         {
            owner.goPrev();
         }
         
      }
      
      //bottom collisions
      for (int i = owner.ch.getX() - 1; i < owner.ch.getX() + w + 1; i ++)
      {
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.BLACK) < 20)
         {
            owner.ch.setY(owner.ch.getY() - 5);;
         }
         
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.GREEN) < 20)
         {
            owner.goNext();
         }
         
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.RED) < 20)
         {
            owner.goPrev();
         }
         
      }
   } 
   
   public boolean enemyCollisions(Enemy e)
   {
      int vis = e.getVisibility();
      boolean xOverlap = false;
      boolean yOverlap = false;
      if ((e.getX() - vis < owner.ch.getX() && owner.ch.getX() < e.getX() + e.getWidth() + vis) || (e.getX() - vis < owner.ch.getX() + owner.ch.getWidth() && owner.ch.getX() + owner.ch.getWidth() < e.getX() + e.getWidth() + vis))
      {
         xOverlap = true;
      }
      if ((e.getY() - vis < owner.ch.getY() && owner.ch.getY() < e.getY() + e.getHeight() + vis) || (e.getY() - vis < owner.ch.getY() + owner.ch.getHeight() && owner.ch.getY() + owner.ch.getHeight()< e.getY() + e.getHeight() + vis))
      {
         yOverlap = true;
      }
      if (xOverlap && yOverlap)
      {
         return true;
      }
      return false;
   }
      
   public void drawMe(Graphics g)
   {
      g.drawImage(image, 0, 0, owner.getWidth(), owner.getHeight(), null);
      for (Enemy enemy : enemies)
      {
         enemy.step();
         boolean there = enemyCollisions(enemy);
         if (there)
         {
         // essentially get rid of the enemy
            enemy.setX(-100);
            System.out.println("straight to hell");
         }
         else
         {
            enemy.drawMe(g);
         }
         
         
      }
   }
}