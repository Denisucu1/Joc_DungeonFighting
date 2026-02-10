package entities;

import mygame.GamePanel;
import input.KeyHandler;
import javax.imageio.ImageIO; // IMPORT NOU
import java.awt.*;
import java.awt.image.BufferedImage; // IMPORT NOU
import java.io.IOException; // IMPORT NOU
import java.io.InputStream; // IMPORT NOU

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    int hasKey = 0;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        // Începem în mijlocul hărții mari (50x50)
        worldX = gp.tileSize * 25;
        worldY = gp.tileSize * 25;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            String path = "/player/Yris_princess.png";
            BufferedImage spriteSheet = null;

            // 1. Încercăm din resursele interne (src/main/resources)
            InputStream is = getClass().getResourceAsStream(path);

            if (is != null) {
                spriteSheet = ImageIO.read(is);
            } else {
                // 2. Fallback către folderul extern 'res'
                java.io.File f = new java.io.File("res" + path);
                if (f.exists()) {
                    spriteSheet = ImageIO.read(f);
                }
            }

            if (spriteSheet != null) {
                int width = 32;
                int height = 32;

                // Tăierea sprite-urilor
                down1 = spriteSheet.getSubimage(0, 0, width, height);
                down2 = spriteSheet.getSubimage(width, 0, width, height);
                left1 = spriteSheet.getSubimage(0, height, width, height);
                left2 = spriteSheet.getSubimage(width, height, width, height);
                right1 = spriteSheet.getSubimage(0, height * 2, width, height);
                right2 = spriteSheet.getSubimage(width, height * 2, width, height);
                up1 = spriteSheet.getSubimage(0, height * 3, width, height);
                up2 = spriteSheet.getSubimage(width, height * 3, width, height);
            } else {
                System.err.println("EROARE: Imaginea nu a fost găsită!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";

            collisionOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // REPARAT: Adăugăm animația picioarelor
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // REPARAT: Selectăm imaginea potrivită
        switch (direction) {
            case "up":    image = (spriteNum == 1) ? up1 : up2; break;
            case "down":  image = (spriteNum == 1) ? down1 : down2; break;
            case "left":  image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        if (image != null) {
            // Desenăm personajul
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            // Fallback: dacă tot nu avem imagine, desenăm pătratul alb
            g2.setColor(Color.WHITE);
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;

            switch (objectName) {
                case "Key":
                    hasKey++;
                    gp.obj[i] = null;
                    System.out.println("Ai găsit o cheie! Total: " + hasKey);
                    break;
            }
        }
    }
}