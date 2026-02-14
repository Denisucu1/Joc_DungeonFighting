package object;

import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class OBJ_Chest extends SuperObject {
    public OBJ_Chest() {
        name = "Chest";
        images = new BufferedImage[5];

        int size = (int)(48 * scale);
        int offset = (48 - size) / 2;
        solidArea = new Rectangle(offset, offset, size, size);

        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/Chest.png"));
            for (int i = 0; i < 5; i++) {
                images[i] = sheet.getSubimage(i * 40, 0, 40, 25);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}