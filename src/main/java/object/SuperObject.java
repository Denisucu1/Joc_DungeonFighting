package object;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import mygame.GamePanel;

public class SuperObject {
    public BufferedImage[] images;
    public String name;
    public boolean collision = false;
    public double worldX, worldY;
    public double scale = 1.0;

    // --- VARIABILELE PENTRU ANIMAȚIE (Reintroduse aici) ---
    public int frameIndex = 0;
    public boolean animating = false;
    public int animationCounter = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void draw(Graphics2D g2, GamePanel gp) {

        // --- LOGICA DE UPDATE PENTRU ANIMAȚIE ---
        if (animating && images != null) {
            animationCounter++;
            if (animationCounter > 10) { // Controlează viteza (10 cadre)
                if (frameIndex < images.length - 1) {
                    frameIndex++;
                } else {
                    // Dacă e ușă sau cufăr, rămâne pe ultimul cadru (deschis)
                    animating = false;
                }
                animationCounter = 0;
            }
        }

        // Calculăm dimensiunea afișată
        int drawWidth = (int)(gp.tileSize * scale);
        int drawHeight = (int)(gp.tileSize * scale);

        // Coordonatele pe ecran
        int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
        int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);

        // Clamping logic (pentru margini)
        int tempX = screenX;
        int tempY = screenY;
        if (gp.player.screenX > gp.player.worldX) tempX = (int) worldX;
        if (gp.player.screenY > gp.player.worldY) tempY = (int) worldY;

        int rightOffset = gp.screenWidth - gp.player.screenX;
        if (rightOffset > gp.worldWidth - gp.player.worldX) {
            tempX = (int) (gp.screenWidth - (gp.worldWidth - worldX));
        }
        int bottomOffset = gp.screenHeight - gp.player.screenY;
        if (bottomOffset > gp.worldHeight - gp.player.worldY) {
            tempY = (int) (gp.screenHeight - (gp.worldHeight - worldY));
        }

        if (images != null && images[frameIndex] != null) {
            g2.drawImage(images[frameIndex], tempX, tempY, drawWidth, drawHeight, null);
        }
    }
}