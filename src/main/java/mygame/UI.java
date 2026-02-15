package mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import object.OBJ_Key;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40;
    BufferedImage keyImage;
    public String currentDialogue = "";

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.images[0];
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.playState) {
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);
        }

        if (gp.gameState == gp.dialogueState) {
            drawDialogueWindow();
        }
    }

    public void drawDialogueWindow() {
        // Poziția și mărimea ferestrei
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        // Fundal negru transparent
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        // Margine albă
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

        // Text
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        int textX = x + gp.tileSize;
        int textY = y + gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
    }
}