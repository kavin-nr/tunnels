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
      
      Projectile strongAmmo = new Projectile(20, 20, "img/proj/Ammo.png", "img/proj/Ammo.png", 50, 5000, 4, 6);
      Projectile medAmmo = new Projectile(20, 20, "img/proj/Ammo.png", "img/proj/Ammo.png", 35, 8000, 6, 8);
      Projectile weakAmmo = new Projectile(25, 25, "img/proj/Ammo.png", "img/proj/Ammo.png", 25, 8000, 4, 8);
      Projectile kingAmmo = new Projectile(25, 25, "img/proj/Ammo.png", "img/proj/Ammo.png", 15, 3000, 6, 10);

      Projectile bone = new Projectile(50, 16, "img/proj/Bone.png", "img/proj/Bone.png", 15, 400, 8, 12);;
      Projectile zomb = new Projectile(50, 16, "img/proj/Knife.png", "img/proj/KnifeL.png", 10, 500, 10, 12);
      Projectile bat = new Projectile(40, 20, "img/proj/Bat.png", "img/proj/BatL.png", 10, 750, 4, 5);
      Projectile reap = new Projectile(50, 40, "img/sprites/Spirit1.png", "img/sprites/Spirit1L.png", 8, 300, 3, 5);
      Projectile slimeball = new Projectile(32, 30, "img/proj/Slimeball.png", "img/proj/SlimeballL.png", 10, 500, 5, 7);
      Projectile blaster = new Projectile(48, 16, "img/proj/Blaster.png", "img/proj/Blaster.png", 15, 300, 9, 11);
      Projectile math = new Projectile(26, 27, "img/proj/Sigma.png", "img/proj/Pi.png", 20, 500,  5, 6);
      Projectile surge = new Projectile(50, 50, "img/proj/Surge.png", "img/proj/Surge.png", 30, 400, 9, 12);
      Projectile spear = new Projectile(100, 18, "img/proj/Spear.png", "img/proj/SpearL.png", 15, 300, 10, 15);
      Projectile fireball = new Projectile(140, 40, "img/proj/Fireball.png", "img/proj/FireballL.png", 25, 500, 9, 10);

      Enemy Ghost1 = new Enemy("A ghost", 330, 260, 75, 75, 30, "img/sprites/Spirit1L.png", "img/sprites/Spirit2L.png", bat, strongAmmo);
      Enemy Ghost2 = new Enemy("A ghost", 550, 225, 75, 75, 40, "img/sprites/Spirit1.png", "img/sprites/Spirit2.png", bat, strongAmmo);
      Enemy Skeleton1 = new Enemy("A skeleton", 230, 365, 100, 100, 30, "img/sprites/Skeleton1.png", "img/sprites/Skeleton2.png", bone, strongAmmo);
      Enemy Skeleton2 = new Enemy("A skeleton", 570, 175, 100, 100, 30, "img/sprites/Skeleton1.png", "img/sprites/Skeleton2.png", bone, strongAmmo);
      Enemy Zombie1 = new Enemy("A zombie", 325, 255, 100, 100, 30, "img/sprites/ArmedZombie1.png", "img/sprites/ArmedZombie2.png", zomb, strongAmmo);
      
      Enemy Reaper = new Enemy("THE DEATH REAPER", 400, 250, 150, 150, 30, "img/sprites/Reaper1.png", "img/sprites/Reaper2.png", reap, medAmmo);
      
      Enemy Slime1 = new Enemy("A slime", 300, 295, 100, 100, 90, "img/sprites/Slime1.png", "img/sprites/Slime2.png", slimeball, strongAmmo);
      Enemy Slime2 = new Enemy("A slime", 700, 290, 100, 100, 50, "img/sprites/Slime1.png", "img/sprites/Slime2.png", slimeball, strongAmmo);
      Enemy Slime3 = new Enemy("A slime", 265, 275, 100, 100, 75, "img/sprites/Slime1.png", "img/sprites/Slime2.png", slimeball, strongAmmo);
      Enemy Destroyer1 = new Enemy("Destroyer Alpha", 560, 100, 175, 200, 50, "img/sprites/Destroyer1.png", "img/sprites/Destroyer2.png", blaster, medAmmo);
      Enemy Calculator1 = new Enemy("TI-83", 550, 330, 125, 125, 50, "img/sprites/Calculator1.png", "img/sprites/Calculator2.png", math, medAmmo);
      Enemy Calculator2 = new Enemy("TI-84", 255, 310, 125, 125, 50, "img/sprites/Calculator1.png", "img/sprites/Calculator2.png", math, medAmmo);
      Enemy Calculator3 = new Enemy("TI-85 Plus CE", 610, 400, 125, 125, 50, "img/sprites/Calculator1.png", "img/sprites/Calculator2.png", math, medAmmo);
      Enemy Slime4 = new Enemy("A slime", 165, 230, 100, 100, 50, "img/sprites/Slime1.png", "img/sprites/Slime2.png", slimeball, strongAmmo);
      Enemy Slime5 = new Enemy("A slime", 570, 270, 100, 100, 75, "img/sprites/Slime1.png", "img/sprites/Slime2.png", slimeball, strongAmmo);
      Enemy Destroyer2 = new Enemy("Destroyer Beta", 175, 215, 175, 200, 20, "img/sprites/Destroyer1.png", "img/sprites/Destroyer2.png", blaster, medAmmo);
      Enemy Destroyer3 = new Enemy("Destroyer Gamma", 560, 100, 175, 200, 50, "img/sprites/Destroyer1.png", "img/sprites/Destroyer2.png", blaster, medAmmo);

      Enemy Inferno = new Enemy("INFERNO-CLASS DESTROYER", 335, 140, 180, 300, 50, "img/sprites/Inferno1.png", "img/sprites/Inferno2.png", surge, weakAmmo);

      Enemy Guard1 = new Enemy("Royal Guard", 255, 130, 100, 100, 100, "img/sprites/DragonGuard1.png", "img/sprites/DragonGuard2.png", spear, weakAmmo);
      Enemy Guard2 = new Enemy("Royal Guard", 545, 340, 100, 100, 100, "img/sprites/DragonGuard1.png", "img/sprites/DragonGuard2.png", spear, weakAmmo);
      Enemy King =  new Enemy("THE DRAGON KING", 390, 280, 100, 100, 30, "img/sprites/DragonKing1.png", "img/sprites/DragonKing2.png", fireball, kingAmmo);

      Enemy Portal = new Enemy("Win", 410, 50, 56, 100, 10, "img/sprites/Portal1.png", "img/sprites/Portal2.png", bone, strongAmmo);

      Map map0 = new Map("maps/display/Display0.png", "maps/hitboxes/Hitbox0.png", 0, 0, 450, 250, this);
      Map map1 = new Map("maps/display/Display1.png", "maps/hitboxes/Hitbox1.png", 315, 400, 565, 100, this);
      Map map2 = new Map("maps/display/Display2.png", "maps/hitboxes/Hitbox2.png", 180, 515, 730, 275, this);
      Map map3 = new Map("maps/display/Display3.png", "maps/hitboxes/Hitbox3.png", 60, 370, 700, 215, this);
      Map map4 = new Map("maps/display/Display4.png", "maps/hitboxes/Hitbox4.png", 55, 285, 605, 565, this);
      Map map5 = new Map("maps/display/Display5.png", "maps/hitboxes/Hitbox5.png", 240, 100, 735, 350, this);
      Map map6 = new Map("maps/display/Display6.png", "maps/hitboxes/Hitbox6.png", 55, 295, 655, 295, this);
      Map map7 = new Map("maps/display/Display7.png", "maps/hitboxes/Hitbox7.png", 60, 295, 700, 295, this);
      Map map8 = new Map("maps/display/Display8.png", "maps/hitboxes/Hitbox8.png", 80, 275, 640, 90, this);
      Map map9 = new Map("maps/display/Display9.png", "maps/hitboxes/Hitbox9.png", 145, 500, 685, 295, this);
      Map map10 = new Map("maps/display/Display10.png", "maps/hitboxes/Hitbox10.png", 60, 295, 645, 570, this);
      Map map11 = new Map("maps/display/Display11.png", "maps/hitboxes/Hitbox11.png", 165, 75, 720, 255, this);
      Map map12 = new Map("maps/display/Display12.png", "maps/hitboxes/Hitbox12.png", 60, 295, 650, 100, this);
      Map map13 = new Map("maps/display/Display13.png", "maps/hitboxes/Hitbox13.png", 420, 545, 420, 55, this);
      Map map14 = new Map("maps/display/Display14.png", "maps/hitboxes/Hitbox14.png", 255, 630, 0, 0, this);
      Map map15 = new Map("maps/display/Display15.png", "maps/hitboxes/Hitbox15.png", 490, 480, 0, 0, this);
      
      
      map1.addEnemy(Ghost1);
      map1.addEnemy(Ghost2);
      
      map3.addEnemy(Skeleton1);
      map3.addEnemy(Skeleton2);
      
      map4.addEnemy(Zombie1);
      
      map5.addEnemy(Reaper);
      
      map7.addEnemy(Slime1);
      map7.addEnemy(Slime2);
      
      map8.addEnemy(Slime3);
      map8.addEnemy(Destroyer1);
      
      map9.addEnemy(Calculator1);

      map10.addEnemy(Calculator2);
      map10.addEnemy(Calculator3);

      map11.addEnemy(Slime4);
      map11.addEnemy(Slime5);

      map12.addEnemy(Destroyer2);
      map12.addEnemy(Destroyer3);

      map13.addEnemy(Inferno);

      map14.addEnemy(Guard1);
      map14.addEnemy(Guard2);

      map15.addEnemy(King);
      map15.addEnemy(Portal);

      mapList = new ArrayList<Map>();
      mapList.add(map0);
      mapList.add(map1);
      mapList.add(map2);
      mapList.add(map3);
      mapList.add(map4);
      mapList.add(map5);
      mapList.add(map6);
      mapList.add(map7);
      mapList.add(map8);
      mapList.add(map9);
      mapList.add(map10);
      mapList.add(map11);
      mapList.add(map12);
      mapList.add(map13);
      mapList.add(map14);
      mapList.add(map15);
      
      
      
      currentMap = mapList.get(13);
      
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
         outfile.println("" + owner.getPlaytime());
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
         owner.setPlaytime(Integer.parseInt(infile.nextLine().strip()));
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
               if (!intString.equals(""))
               {
               ((mapList.get(mapIndex)).getEnemy(enemyIndex)).setX(Integer.parseInt(intString));
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
   }
   
   public void goPrev()
   {
      Map prevMap = mapList.get(mapList.indexOf(currentMap) - 1);
      ch.setX(prevMap.getNextX());
      ch.setY(prevMap.getNextY());
      currentMap = prevMap;   
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
