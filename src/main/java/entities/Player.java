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
    public int hasKey = 0;

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

            // Stabilim direcția pentru animație
            if (keyH.upPressed) direction = "up";
            if (keyH.downPressed) direction = "down";
            if (keyH.leftPressed) direction = "left";
            if (keyH.rightPressed) direction = "right";

            // Normalizăm viteza pentru diagonală (să nu meargă mai repede)
            double currentSpeed = speed;
            if ((keyH.upPressed || keyH.downPressed) && (keyH.leftPressed || keyH.rightPressed)) {
                currentSpeed = speed * 0.7071; // Aproximativ 1/sqrt(2)
            }

            // Verificăm obiectele
            collisionOn = false;
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // MIȘCARE PE AXA Y
            if (keyH.upPressed || keyH.downPressed) {
                String tempDir = direction;
                direction = keyH.upPressed ? "up" : "down";
                collisionOn = false;
                gp.cChecker.checkTile(this);
                if (!collisionOn) {
                    if (direction.equals("up")) worldY -= currentSpeed;
                    else worldY += currentSpeed;
                }
                direction = tempDir;
            }

            // MIȘCARE PE AXA X
            if (keyH.leftPressed || keyH.rightPressed) {
                String tempDir = direction;
                direction = keyH.leftPressed ? "left" : "right";
                collisionOn = false;
                gp.cChecker.checkTile(this);
                if (!collisionOn) {
                    if (direction.equals("left")) worldX -= currentSpeed;
                    else worldX += currentSpeed;
                }
                direction = tempDir;
            }

            // Animație sprite-uri
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // Switch-ul tău pentru imagini (up1, down1, etc.)
        switch (direction) {
            case "up":    image = (spriteNum == 1) ? up1 : up2; break;
            case "down":  image = (spriteNum == 1) ? down1 : down2; break;
            case "left":  image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        double drawScreenX = screenX;
        double drawScreenY = screenY;

        // Clamping pe X
        if (screenX > worldX) drawScreenX = worldX;
        else if (gp.screenWidth - screenX > gp.worldWidth - worldX) {
            drawScreenX = gp.screenWidth - (gp.worldWidth - worldX);
        }

        if (screenY > worldY) drawScreenY = worldY;
        else if (gp.screenHeight - screenY > gp.worldHeight - worldY) {
            drawScreenY = gp.screenHeight - (gp.worldHeight - worldY);
        }

        g2.drawImage(image, (int)drawScreenX, (int)drawScreenY, gp.tileSize, gp.tileSize, null);
    }

    public void pickUpObject(int i) {
        // Adăugăm: && gp.gameState == gp.playState
        // Asta înseamnă: "Dacă atingi ceva, dar NUMAI dacă nu suntem deja într-un dialog"
        if (i != 999 && gp.gameState == gp.playState) {

            String objectName = gp.obj[i].name;

            switch (objectName) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.currentDialogue = "Ai luat o cheie!";
                    gp.gameState = gp.dialogueState; // Schimbăm starea ca să oprim spam-ul
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.playSE(3);
                        gp.obj[i].animating = true;
                        gp.obj[i].collision = false;
                        hasKey--;
                    } else {
                        gp.ui.currentDialogue = "Ușa e încuiată.\nGăsește o cheie!";
                        gp.gameState = gp.dialogueState;
                    }
                    break;
                case "Chest":
                    gp.playSE(2);
                    gp.obj[i].animating = true;
                    gp.ui.currentDialogue = "BRAVO!\nAi terminat nivelul!";
                    gp.gameState = gp.dialogueState;
                    break;
            }
        }
    }
}