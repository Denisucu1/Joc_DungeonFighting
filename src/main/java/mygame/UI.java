package mygame;

import java.awt.*;
import java.awt.image.BufferedImage;
import object.OBJ_Key;

public class UI {
    GamePanel gp;
    Font arial_40;
    BufferedImage keyImage;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.images[0];
    }

    public void draw(Graphics2D g2) {
        // Desenăm iconița cheii în colțul stânga sus
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasKey, 74, 65);
    }
}