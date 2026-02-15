package mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import object.OBJ_Key;
import javax.imageio.ImageIO;
import java.io.IOException;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40;
    public String currentDialogue = "";

    // VARIABILELE PENTRU MENIU
    BufferedImage keyImage;
    BufferedImage menuBg;
    public int commandNum = 0;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);

        // Încărcăm imaginea cheii folosind obiectul OBJ_Key existent
        OBJ_Key key = new OBJ_Key();
        this.keyImage = key.images[0];

        try {
            menuBg = ImageIO.read(getClass().getResourceAsStream("/menu/menu_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        // 1. ECRAN TITLU
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        // 2. ÎN TIMPUL JOCULUI (Play) sau DIALOGULUI
        // Folosim || (SAU) pentru ca cheile să apară în ambele stări
        if (gp.gameState == gp.playState || gp.gameState == gp.dialogueState) {

            // Desenăm cheia în colțul stânga sus
            if (keyImage != null) {
                g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
                g2.drawString("x " + gp.player.hasKey, 74, 65);
            }

            // Dacă suntem în dialog, desenăm și fereastra de text
            if (gp.gameState == gp.dialogueState) {
                drawDialogueWindow();
            }
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

    public void drawTitleScreen() {
        // 1. FUNDALUL (Imaginea ta)
        if (menuBg != null) {
            g2.drawImage(menuBg, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }

        // 2. TITLUL (Cu umbră pentru aspect profesional)
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "AVENTURA LUI YRIS";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.BLACK); // Umbra
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.WHITE); // Textul principal
        g2.drawString(text, x, y);

        // 3. MENIUL (Poziționat în centrul ecranului)
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        // START
        text = "START JOC";
        x = getXforCenteredText(text);
        y = (int)(gp.screenHeight / 1.6);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            // Desenăm personajul Yris pe post de cursor la stânga textului
            g2.drawImage(gp.player.down1, x - gp.tileSize, y - 35, gp.tileSize, gp.tileSize, null);
        }

        // EXIT
        text = "IEȘIRE";
        x = getXforCenteredText(text);
        y += gp.tileSize * 1.5;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawImage(gp.player.down1, x - gp.tileSize, y - 35, gp.tileSize, gp.tileSize, null);
        }
    }

    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}