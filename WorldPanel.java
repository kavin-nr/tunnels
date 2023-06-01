import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

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
   
   private ArrayList<Map> mapList;
   private Map currentMap;
   
   private TunnelsPanel owner;
   private boolean previousMuteState;
   private boolean isFocused;
   private int saved = -1;
   
   private File savefile = new File("save.txt");
   
   //constructors
   public WorldPanel(TunnelsPanel o)
   {
      owner = o;
      previousMuteState = false;
   
      setPreferredSize(new Dimension(width, height));
      isFocused = false;

   
      myImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
      myBuffer = myImage.getGraphics(); 
      
      Projectile strongAmmo = new Projectile(20, 20, "img/proj/Ammo.png", 50, 5000, 4, 6);
      Projectile reaperAmmo = new Projectile(20, 20, "img/proj/Ammo.png", 35, 8000, 6, 8);
      
      Projectile bone = new Projectile(50, 16, "img/proj/Bone.png", 15, 500, 7, 10);
      Projectile zomb = new Projectile(50, 16, "img/proj/Knife.png", 10, 500, 10, 12);
      Projectile bat = new Projectile(40, 20, "img/proj/Bat.png", 10, 750, 4, 5);
      Projectile reap = new Projectile(50, 16, "img/proj/Knife.png", 8, 300, 3, 5);
            
      Enemy Ghost1 = new Enemy(330, 260, 75, 75, 30, "img/sprites/Spirit1L.png", "img/sprites/Spirit2L.png", bat, strongAmmo);
      Enemy Ghost2 = new Enemy(550, 225, 75, 75, 30, "img/sprites/Spirit1.png", "img/sprites/Spirit2.png", bat, strongAmmo);
      Enemy Skeleton1 = new Enemy(230, 365, 100, 100, 30, "img/sprites/Skeleton1.png", "img/sprites/Skeleton2.png", bone, strongAmmo);
      Enemy Skeleton2 = new Enemy(570, 175, 100, 100, 30, "img/sprites/Skeleton1.png", "img/sprites/Skeleton2.png", bone, strongAmmo);
      Enemy Zombie1 = new Enemy(325, 255, 100, 100, 30, "img/sprites/ArmedZombie1.png", "img/sprites/ArmedZombie2.png", zomb, strongAmmo);
      
      Enemy Reaper = new Enemy(400, 250, 150, 150, 30, "img/sprites/Reaper1.png", "img/sprites/Reaper2.png", reap, reaperAmmo);
      
      Map map0 = new Map("maps/display/Display0.png", "maps/hitboxes/Hitbox0.png", 0, 0, 450, 250, this);
      Map map1 = new Map("maps/display/Display1.png", "maps/hitboxes/Hitbox1.png", 315, 400, 565, 100, this);
      Map map2 = new Map("maps/display/Display2.png", "maps/hitboxes/Hitbox2.png", 180, 515, 730, 275, this);
      Map map3 = new Map("maps/display/Display3.png", "maps/hitboxes/Hitbox3.png", 60, 370, 700, 215, this);
      Map map4 = new Map("maps/display/Display4.png", "maps/hitboxes/Hitbox4.png", 55, 285, 605, 565, this);
      Map map5 = new Map("maps/display/Display5.png", "maps/hitboxes/Hitbox5.png", 240, 100, 735, 350, this);
      
      map1.addEnemy(Ghost1);
      map1.addEnemy(Ghost2);
      
      map3.addEnemy(Skeleton1);
      map3.addEnemy(Skeleton2);
      
      map4.addEnemy(Zombie1);
      
      map5.addEnemy(Reaper);
      
      mapList = new ArrayList<Map>();
      mapList.add(map0);
      mapList.add(map1);
      mapList.add(map2);
      mapList.add(map3);
      mapList.add(map4);
      mapList.add(map5);
      
      currentMap = mapList.get(0);
      
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
      g.setColor(Color.WHITE);
      g.setFont(new Font("Monospaced", Font.BOLD, 30));
      if (0 <= saved && saved <= 30)
      { 
         g.drawString("Game saved.", 10, 40);
         saved++;
      }
      else if (30 < saved && saved <= 60)
      {
         g.drawString("Player healed.", 10, 40); 
         saved++;
      }
      
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   public boolean getFocus()
   {
      return isFocused;
   }
   
   public void setFocus(boolean f)
   {
      isFocused = f;
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
   
      boolean muteState = owner.getMute();
      if (previousMuteState != muteState)
      {
         // The player has pressed "M"
         if (muteState)
         {
            owner.StopMusic1();
         }
         else
         {
            owner.StartMusic1();
         }
      }
      previousMuteState = muteState;
      repaint();
   }
   
   public void save()
   {
      try
      {
         savefile.createNewFile();
         PrintWriter outfile = new PrintWriter( new FileWriter(savefile));
         outfile.println(mapList.indexOf(currentMap));
         outfile.println("" + ch.getX());
         outfile.println("" + ch.getY());
         for (Map map : mapList)
         {
            ArrayList<Enemy> enemies = map.getEnemies();
            outfile.print("\n");
            for (Enemy enemy : enemies)
            {
               outfile.print(enemy.getX() + " ");
            }
         }
         outfile.close();
         
         if (mapList.indexOf(currentMap) != 0)
         {
            saved = 0;
         }
      }
      catch (IOException ex) {}
   }
   
   public void load()
   {
      try
      {
         Scanner infile = new Scanner(savefile);
         currentMap = mapList.get(Integer.parseInt(infile.nextLine().strip()));
         ch.setX(Integer.parseInt(infile.nextLine().strip()));
         ch.setY(Integer.parseInt(infile.nextLine().strip()));
         ch.setHealth(100);
         infile.nextLine();
         int mapIndex = 0;
         while (infile.hasNextLine())
         {
            String[] thisLine = infile.nextLine().strip().split(" ");
            int enemyIndex = 0;
            for (String intString : thisLine)
            {
               System.out.println(intString);
               if (intString.equals("1000"))
               {
                  ((mapList.get(mapIndex)).getEnemy(enemyIndex)).setX(1000);
               }
               enemyIndex++;
            }
            mapIndex++;
         }
         infile.close();
      }
      catch (FileNotFoundException ex)
      {
         save();
      }
   }
   
   public void goNext()
   {
      Map nextMap = mapList.get(mapList.indexOf(currentMap) + 1);
      ch.setX(nextMap.getPrevX());
      ch.setY(nextMap.getPrevY());
      currentMap = nextMap;   
      System.out.println("next");
   }
   
   public void goPrev()
   {
      Map prevMap = mapList.get(mapList.indexOf(currentMap) - 1);
      ch.setX(prevMap.getNextX());
      ch.setY(prevMap.getNextY());
      currentMap = prevMap;   
      System.out.println("prev");
   }
   
   public void savepoint()
   {
      // Interacting with a savepoint heals player to full health and saves game
      ch.setHealth(100);
      save();
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
   
   public class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)  
      {
         if (isFocused)
         {
            animate();
         }
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
         if(e.getKeyCode() == KeyEvent.VK_LEFT && left) // If the user lets go of the left arrow
         {
            ch.setDX(ch.getDX() + 5);  //Again: add 2, don't set to 0 precisely.  Explanation in the assignment.
            left = false;  //User is no longer holding the left key, so set this back to false.
         }
         
         if (e.getKeyCode() == KeyEvent.VK_RIGHT && right)
         {
            ch.setDX(ch.getDX() - 5);
            right = false;
         }
         
         if (e.getKeyCode() == KeyEvent.VK_UP && up)
         {
            ch.setDY(ch.getDY() + 5);
            up = false;
         }
      
         if (e.getKeyCode() == KeyEvent.VK_DOWN && down)
         {
            ch.setDY(ch.getDY() - 5);
            down = false;
         }
         
      }
   }
   
}
