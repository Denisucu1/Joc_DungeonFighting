package mygame;

import javax.swing.*;
import java.awt.*;
import input.KeyHandler;
import entities.Player;
import tile.TileManager;
import object.SuperObject;


public class GamePanel extends JPanel implements Runnable {

    Sound music = new Sound();
    Sound se = new Sound();


    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int dialogueState = 2;

    public AssetSetter aSetter = new AssetSetter(this);
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    public UI ui = new UI(this);

    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;

    public Player player = new Player(this, keyH);
    public TileManager tileM = new TileManager(this);;
    public CollisionChecker cChecker;

    public SuperObject obj[] = new SuperObject[10];

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.gameState = playState;
        this.gameState = titleState;

        tileM = new TileManager(this);
        cChecker = new CollisionChecker(this);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            // Dacă obiectele tale au animație, asigură-te că le dai update aici
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null && obj[i].animating) {
                    // Logica ta de schimbare a cadrelor (frames) pentru ușa/chest
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // TITLE STATE
        if (gameState == titleState) {
            ui.draw(g2); // Aici ui.draw va chema drawTitleScreen()
        }
        // PLAY STATE & DIALOGUE
        else {
            tileM.draw(g2);
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) obj[i].draw(g2, this);
            }
            player.draw(g2);
            ui.draw(g2);
        }
        g2.dispose();
    }

    public void setupGame() {
        aSetter.setObject();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.setVolume(-20.0f); // Încearcă -20.0f sau -30.0f pentru a fi mai încet
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) { // SE = Sound Effect
        se.setFile(i);
        se.play();
    }

}