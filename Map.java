import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;

/**
*Class that displays the maps for the world
*/
public class Map
{
   /**
   * Sets up an image icon 
   */
   
   private ImageIcon imageSrc, hitboxSrc;
   /**
   * Sets up a buffered image
   */
    
   private BufferedImage image, hitbox;
   /**
   * Sets up graphics
   */
   
   private Graphics imageGr, hitboxGr;
   /**
   * Sets up the x and y values for the previous and next maps
   */
      
   private int prevX, prevY, nextX, nextY;
   /**
   * Sets up world panel as the owner
   */
  
   private WorldPanel owner;
   /**
   * Sets up an arraylist of enemies
   */   
   private ArrayList<Enemy> enemies;
   
   /**
   * Initializes the map
   */
  
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
    /**
   * Gets previous x 
   */
    
   public int getPrevX()
   {
      return prevX;
   }
   /**
   * Gets next x
   */
   
   public int getNextX()
   {
      return nextX;
   }
   /**
   * Gets previous y
   */
   
   public int getPrevY()
   {
      return prevY;
   }
   /**
   * Gets next y 
   */
   
   public int getNextY()
   {
      return nextY;
   }
   /**
   * Gets enemy location
   */
   
   public Enemy getEnemy(int loc)
   {
      return enemies.get(loc);
   }
   /**
   * Gets arraylist of enemies
   */
   
   public ArrayList<Enemy> getEnemies()
   {
      return enemies;
   }
   /**
   * Adds enemies to the map
   */
    
   public void addEnemy(Enemy e)
   {
      enemies.add(e);
   }
   /**
   * Gets the color of the buffered image in a 2D array
   */
   
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
   /**
   * Finds the distance to a certain color
   */
   
   public static int colorDistance(Color one, Color two)
   {
      return (int) (Math.sqrt((Math.pow(two.getRed() - one.getRed(), 2))+(Math.pow(two.getGreen() - one.getGreen(), 2))+(Math.pow(two.getBlue() - one.getBlue(), 2))));
   }
   /**
   * Sets up colisions with colors to take you to different maps, simulate walls, and save the game
   */
   
   public void collisions()
   {
      int w = owner.ch.getWidth();
      int h = owner.ch.getHeight();
      Color[][] map = getArray(hitbox);
      
      // Green is to go to the next map
      // Red is to go to the previous map
      // Blue is to reset health and save to file
      boolean green = false;
      boolean red = false;
      boolean blue = false;
      
      //left color collisions
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 1], Color.GREEN) < 20)
      {
         green = true;
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 1], Color.RED) < 20)
      {
         red = true;
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 1], Color.BLUE) < 20)
      {
         blue = true;
         owner.ch.setX(owner.ch.getX() + 5);
      }
      
      //right color collisions
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 1], Color.GREEN) < 20)
      {
         green = true;
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 1], Color.RED) < 20)
      {
         red = true;
      }
      
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 1], Color.BLUE) < 20)
      {
         blue = true;
         owner.ch.setX(owner.ch.getX() - 5);
      }
   
      //top color collisions
      for (int i = owner.ch.getX(); i < owner.ch.getX() + w; i ++)
      {
         if (colorDistance(map[owner.ch.getY() - 5][i], Color.GREEN) < 20)
         {
            green = true;
         }
         
         if (colorDistance(map[owner.ch.getY() - 5][i], Color.RED) < 20)
         {
            red = true;
         }
         
         if (colorDistance(map[owner.ch.getY() + h - 5][i], Color.BLUE) < 20)
         {
            blue = true;
            owner.ch.setY(owner.ch.getY() + 5);
            break;
         }
         
      }
      
      //bottom color collisions
      for (int i = owner.ch.getX(); i < owner.ch.getX() + w; i ++)
      {
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.GREEN) < 20)
         {
            green = true;
         }
         
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.RED) < 20)
         {
            red = true;
         }
         
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.BLUE) < 20)
         {
            blue = true;
            owner.ch.setY(owner.ch.getY() - 5);
            break;
         }
         
      }
      
      if (blue)
      {
         owner.savepoint();
      }
      
      if (green)
      {
         owner.goNext();
      }
      
      if (red)
      {
         owner.goPrev();
      }
      
      
      
      //left wall collisions
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() - 5], Color.BLACK) < 20)
      {
         owner.ch.setX(owner.ch.getX() + 5);
      }
      
      //right wall collisions    
      if (colorDistance(map[owner.ch.getY() + h][owner.ch.getX() + w + 5], Color.BLACK) < 20)
      {
         owner.ch.setX(owner.ch.getX() - 5);
      }
      
      //top wall collisions 
      for (int i = owner.ch.getX() - 5; i < owner.ch.getX() + w + 5; i ++)
      {
      
         if (colorDistance(map[owner.ch.getY() + h - 5][i], Color.BLACK) < 20)
         {
            owner.ch.setY(owner.ch.getY() + 5);
            break;
         }
      
      }
   
      //bottom wall collisions
      for (int i = owner.ch.getX() - 5; i < owner.ch.getX() + w + 5; i ++)
      {
         if (colorDistance(map[owner.ch.getY() + h + 5][i], Color.BLACK) < 20)
         {
            owner.ch.setY(owner.ch.getY() - 5);
            break;
         }      
      }      
   } 
   /**
   * Sets up collisions with enemies based on their "visibility" of the character
   */
   
   public int enemyCollisions(Enemy e, int index)
   {
      // Visibility is within how many pixels away from an enemy a player has to be to engage in combat
      int vis = e.getVisibility();
      
      // The function checks for overlapping bounds between an enemy's visibility zone and the player himself
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
         owner.goCombat(e);
         System.out.println("straight down");
         return index;
      }
      return -1;
   }
   /**
   * Draws the image
   */
      
   public void drawMe(Graphics g)
   {
      g.drawImage(image, 0, 0, owner.getWidth(), owner.getHeight(), null);
      
      ArrayList<Integer> toRemove = new ArrayList<Integer>();
      int index = 0;
      
      for (Enemy enemy : enemies)
      {
         enemy.step();
         toRemove.add(enemyCollisions(enemy, index));         
         enemy.drawMe(g);
         index++;                  
      }
      /*
      for (int i = 0; i < toRemove.size(); i++)
      {
         if (toRemove.get(i) != -1)
         {
            enemies.remove(enemies.get(toRemove.get(i)));
         }
      }
      */
   }
}