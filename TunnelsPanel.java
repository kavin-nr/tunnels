import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Math;
import javax.sound.sampled.*;
import java.io.*;


public class TunnelsPanel extends JPanel
{
   private TitlePanel title;
   private JFrame owner;
   public WorldPanel world;
   private GameOverPanel gameOver;
   private CombatPanel combat;
   private WinPanel win;
   private Enemy currentEnemy;
   private GuidePanel guide;
   private JPanel blackOverlay;
   private Timer timer;

   private int playtime;
   private Timer playtimeTimer;
   
   private boolean mute;
   private Clip titleClip;
   private Clip ruinsClip;
   private Clip battleClip;
   private Clip victory;
   private Clip death;
   
   private JPanel ready;
   
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
   
   public void newWorld()
   {
      world = new WorldPanel(this);
      playtime = 0;
      world.save();
      goWorld();
   }
   
   public void loadWorld()
   {
      stopDeath();
      gameOver.setFocus(false);
      StopMusic();
      world.load();
      goWorld();
   }
   
   public int getPlaytime()
   {
      return playtime;
   }

   public void setPlaytime(int p)
   {
      playtime = p;
   }

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

   public void StartMusic()
   {
      titleClip.start();
      titleClip.loop(Clip.LOOP_CONTINUOUSLY);
   }
   public void StopMusic()
   {
      titleClip.stop();
   }
   public void StartMusic1()
   {
      ruinsClip.start();
      ruinsClip.loop(Clip.LOOP_CONTINUOUSLY);
   }
   public void StopMusic1()
   {
      ruinsClip.stop();
   }
   public void StartMusic2()
   {
      battleClip.start();  
      battleClip.loop(Clip.LOOP_CONTINUOUSLY); 
   }
   public void StopMusic2()
   {
      battleClip.stop();   
   }

   public void startDeath()
   {
      death.start();
      death.loop(Clip.LOOP_CONTINUOUSLY);
   }
   
   public void stopDeath()
   {
      death.stop();
   }

   public boolean getMute()
   {
      return mute;
   }   
   
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
   
   private class Counter implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         playtime++;
      }
   }
   
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