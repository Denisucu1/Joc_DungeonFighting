package object;

import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class OBJ_Chest extends SuperObject {
    public OBJ_Chest() {
        name = "Chest";
        scale = 0.8; // Setăm scala prima dată
        images = new BufferedImage[5];

        // Definim o zonă solidă fixă (48 e mărimea standard a tile-ului tău)
        // O ajustăm manual pentru cufăr
        solidArea = new Rectangle(4, 4, 40, 40);

        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/Chest.png"));
            for (int i = 0; i < 5; i++) {
                images[i] = sheet.getSubimage(i * 40, 0, 40, 25);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}