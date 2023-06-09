import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Math;
import javax.sound.sampled.*;
import java.io.*;


/**
*Class for the main "owner" panel
*/
public class TunnelsPanel extends JPanel
{
   /**
   * Initializes a title panel
   */
   private TitlePanel title;
   /**
   * Initializes JFrame as the owner
   */   
   private JFrame owner;
   /**
   * Initializes a world panel
   */   
   public WorldPanel world;
   /**
   * Initializes a game over panel
   */   
   private GameOverPanel gameOver;
   /**
   * Initializes a combat panel
   */   
   private CombatPanel combat;
   /**
   * Initializes enemies
   */   
   private Enemy currentEnemy;
   /**
   * Initializes a guide panel
   */   
   private GuidePanel guide;
   /**
   * Initializes a win panel
   */   
   private WinPanel win; 
   /**
   * Initializes the black overlay for transition
   */   
   private JPanel blackOverlay;
   /**
   * Initializes the main timer
   */   
   private Timer timer;
   
   /**
   * Stores playtime in an integer
   */   
   private int playtime;
   /**
   * Initializes timer for total playtime
   */      
   private Timer playtimeTimer;
   /**
   * Boolean that controls whether to mute or not
   */   
   private boolean mute;
   /**
   * Music clip for title screen
   */   
   private Clip titleClip;
   /**
   * Music clip for main world
   */   
   private Clip ruinsClip;
   /**
   * Music clip for battle
   */   
   private Clip battleClip;
   /**
   * Sound effect for winning battle
   */   
   private Clip victory;
   /*
   * Music clip for dying
   */   
   private Clip death;
   /*
   * Music clip for winning the entire game
   */      
   private Clip winner; 
   /**
   * Opens the jpanel that is ready
   */
   private JPanel ready;
   /**
   * Initializes tunnels panel
   */
   public TunnelsPanel(JFrame o)
   {
      owner = o;
      setLayout(new BorderLayout());
      title = new TitlePanel(this);
      guide = new GuidePanel(this);
      gameOver = new GameOverPanel(this);
      win = new WinPanel(this);
      title.setFocus(true);
      add(title);
      
      titleClip = openMusic("music/chill.wav");
      StartMusic();
   
      ruinsClip = openMusic("music/ruins.wav");
   
      battleClip = openMusic("music/battle.wav");
   
      victory = openMusic("music/defeated.wav");
   
      death = openMusic("music/death.wav");
      
      winner = openMusic("music/win.wav");
   
      mute = false;
            
      world = new WorldPanel(this);
      
      playtimeTimer = new Timer(1000, new Counter());
      playtimeTimer.start();
      
      // Key binding for toggling mute
      // Had to do a lot of research on this, the reason that we're not using a KeyListener here is because it has limitations (it only gets input when its in a focused component)
      // We wanted to get arrow key input from the focused component WHILE getting mute input globally
   
      getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('m'), "toggleMute");
      getActionMap().put("toggleMute", 
         new AbstractAction() 
         {
            public void actionPerformed(ActionEvent e)
            {
            // Mute becomes true if it was false, and becomes false if it was true
               mute = !mute;
            }
         });
   }
   
   /**
   * Takes you to start of world
   */
   
   public void newWorld()
   {
      world = new WorldPanel(this);
      world.save();
      goWorld();
   }
   /**
   * Takes you to last save
   */
   
   public void loadWorld()
   {
      stopDeath();
      gameOver.setFocus(false);
      StopMusic();
      world.load();
      goWorld();
   }
   /**
   * Gets the amount playtime
   */
   public int getPlaytime()
   {
      return playtime;
   }
   /**
   * Sets the amount of playtime
   */
   public void setPlaytime(int p)
   {
      playtime = p;
   }
   
   /**
   * Takes you back to the world
   */
  
   public void goWorld() 
   {
    // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, world.getWidth(), world.getHeight());
      add(blackOverlay);
      title.setFocus(false);
      StopMusic();
      world.setFocus(true);
      if (!mute)
      {
         StartMusic1();
      }      
     
      remove(title);
      remove(gameOver);
      ready = world;
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();
   
      
   }
   /**
   * Opens and reads sound files
   */
   
   public Clip openMusic(String filename)
   {
      try
      {
         File file = new File(getClass().getResource(filename).toURI());
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
         Clip c = AudioSystem.getClip();
         c.open(audioInputStream);
         return c;
      }
      catch (Exception e)
      {
         System.err.println(e.getMessage());     
      }
      return null;
   }
   /**
   * Starts first music clip
   */

   public void StartMusic()
   {
      titleClip.start();
      titleClip.loop(Clip.LOOP_CONTINUOUSLY);
   }
   /**
   * Stops first music clip
   */   
   public void StopMusic()
   {
      titleClip.stop();
   }
   /**
   * Starts second music clip
   */
   
   public void StartMusic1()
   {
      ruinsClip.start();
      ruinsClip.loop(Clip.LOOP_CONTINUOUSLY);
   }
   /**
   * Stops second music clip 
   */
   
   public void StopMusic1()
   {
      ruinsClip.stop();
   }
   /**
   * Starts third music clip
   */   
   public void StartMusic2()
   {
      battleClip.start();  
      battleClip.loop(Clip.LOOP_CONTINUOUSLY); 
   }
   /**
   * Stops third music clip
   */   
   public void StopMusic2()
   {
      battleClip.stop();   
   }
   /**
   * Starts death music clip
   */

   public void startDeath()
   {
      death.start();
      death.loop(Clip.LOOP_CONTINUOUSLY);
   }
   /**
   * Stops death music clip
   */
   
   public void stopDeath()
   {
      death.stop();
   }
   /**
   * Starts win music clip
   */

   public void startWin()
   {
      winner.start();
      winner.loop(Clip.LOOP_CONTINUOUSLY);
   }
   /**
   * Stops win music clip
   */
   public void stopWin()
   {
      winner.stop();
   }
   
   /**
   * Gets mute value
   */
   public boolean getMute()
   {
      return mute;
   }   
   /**
   * Takes you to title screen
   */   
   public void goTitle()
   {
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, world.getWidth(), world.getHeight());
      add(blackOverlay);
      guide.setFocus(false);
      title.setFocus(true);
      remove(win);     
      remove(guide);
      remove(gameOver);
      ready = title;
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();
   
   }
   /**
   * Takes you to guide screen 
   */   
   public void goGuide()
   {
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, world.getWidth(), world.getHeight());
      add(blackOverlay);
      guide.setFocus(true);
      title.setFocus(false);
     
      remove(title);
      remove(gameOver);
      ready = guide;
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();
   
   }
   /**
   * takes you to combat screen
   */
   public void goCombat(Enemy e) 
   {
   // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, title.getWidth(), title.getHeight());
      add(blackOverlay);
      world.setFocus(false);
      StopMusic1();
      
      remove(world);
      if ((e.getName()).equals("Win"))
      {
         win.setFinishTime(playtime);
         ready = win;
         win.setFocus(true);
         StopMusic1();
         startWin();
      }
      else
      {
         currentEnemy = e;
         if (!mute)
         {
            battleClip = openMusic("music/battle.wav");
            StartMusic2();
         }
         combat = new CombatPanel(e, this);
         combat.setFocus(true);
         ready = combat;
      }
      
    // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();
   }
   
   /**
   * Ends combat
   */   
   public void endCombat(boolean result)
   {
      
      // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, title.getWidth(), title.getHeight());
      add(blackOverlay);
      // Removes the already existing ready panel which should be the existing CombatPanel and adds the existing WorldPanel
      combat.setFocus(false);
      StopMusic2();
      remove(combat);
      if (result)
      {
         ready = world;
         currentEnemy.setX(1000);
         world.setFocus(true);
         if (!mute)
         {
            victory = openMusic("music/defeated.wav");
            victory.start();
         }
         if (!mute)
         {
            StartMusic1();
         }     
      }
      else
      {
         ready = gameOver;
         gameOver.setFocus(true);
         if (!mute)
         {
            startDeath();
         }
      }
      // Use a Timer to gradually increase the alpha value of the black overlay panel
      timer = new Timer(50, new FadeListener()); 
      timer.start();
   }
   
   /**
   * Creates a counter that adds to playtime using an action listener
   */
   
   private class Counter implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         playtime++;
      }
   }
   /**
   * Creates a fading effect for swapping panels
   */
   
   private class FadeListener implements ActionListener
   {
      private int alpha = 0;
      private int fadeCount = 0;
      public void actionPerformed(ActionEvent e) 
      {
         alpha += (int) (Math.pow(fadeCount, 2) / 10);
         fadeCount++;
         
         if (alpha >= 255) 
         {
         // When the alpha value reaches 255, remove the black overlay panel and add the WorldPanel
            remove(blackOverlay);
            add(ready);
            repaint();
            revalidate();
            owner.pack();
            ready.requestFocusInWindow();
            timer.stop();
         } 
         else 
         {
         // Otherwise, set the alpha value of the black overlay panel and repaint the panel
            blackOverlay.setBackground(new Color(0, 0, 0, alpha));
            repaint();
         }
      }
   }

}