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
   private DialoguePanel dialogue;
   public WorldPanel world;
   private GameOverPanel gameOver;
   private JPanel blackOverlay;
   private Timer timer;
   
   private boolean mute;
   private Clip titleClip;
   private Clip ruinsClip;
   private Clip battleClip;
   private Clip victory;
   
   private JPanel ready;
   
   public TunnelsPanel(JFrame o)
   {
      owner = o;
      setLayout(new BorderLayout());
      title = new TitlePanel(this);
      gameOver = new GameOverPanel(this);
      add(title);
      
      titleClip = openMusic("music/chill.wav");
      StartMusic();

      ruinsClip = openMusic("music/ruins.wav");

      battleClip = openMusic("music/battle.wav");

      victory = openMusic("music/defeated.wav");

      mute = false;
      
      dialogue = new DialoguePanel();
      
      world = new WorldPanel(this);
      
      
      
      // Key binding for toggling mute
      // Had to do a lot of research on this, the reason that we're not using a KeyListener here is because it has limitations (it only gets input when its in a focused component)
      // We wanted to get arrow key input from the focused component WHILE getting mute input globally
 
      getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('m'), "toggleMute");
      getActionMap().put("toggleMute", new AbstractAction() 
      {
         public void actionPerformed(ActionEvent e)
         {
            // Mute becomes true if it was false, and becomes false if it was true
            mute = !mute;
            System.out.println("MUTED " + mute);
         }
      });
   }
   
   public void newWorld()
   {
      world = new WorldPanel(this);
      goWorld();
   }
   
   public void loadWorld()
   {
      world.load();
      goWorld();
   }
   
   public void goWorld() 
   {
    // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, world.getWidth(), world.getHeight());
      add(blackOverlay);
      StopMusic();
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
   
   public boolean getMute()
   {
      return mute;
   }   
   
   public void goCombat(Enemy e) 
   {
   // Create the black overlay panel with 0% alpha
      blackOverlay = new JPanel();
      blackOverlay.setBounds(0, 0, title.getWidth(), title.getHeight());
      add(blackOverlay);
      remove(world);

      StopMusic1();

      ready = new CombatPanel(e, this);
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
      remove(ready);
      if (result)
      {
         ready = world;
         if (!mute)
         {
            victory.start();
         }
         // Use a Timer to gradually increase the alpha value of the black overlay panel
         timer = new Timer(50, new FadeListener()); 
         timer.start();         
      }
      else
      {
         ready = gameOver;
         // Use a Timer to gradually increase the alpha value of the black overlay panel
         timer = new Timer(50, new FadeListener()); 
         timer.start();
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
            System.out.println(alpha);
            repaint();
         }
      }
   }

}