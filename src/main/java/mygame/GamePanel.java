package mygame;

import javax.swing.*;
import java.awt.*;
import input.KeyHandler;
import entities.Player;
import tile.TileManager;
import object.SuperObject;


public class GamePanel extends JPanel implements Runnable {

    public int gameState;
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
        }
        if (gameState == dialogueState) {
            // Jocul rămâne înghețat pe durata dialogului
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. Desenează harta
        tileM.draw(g2);

        // 2. Desenează obiectele
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // 3. Desenează jucătorul
        player.draw(g2);

        // 4. Desenează interfața (UI) - ULTIMA, ca să fie deasupra tuturor
        ui.draw(g2);

        g2.dispose();
    }

    public void setupGame() {
        aSetter.setObject();
    }
}