package object;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class OBJ_Door extends SuperObject {
    public OBJ_Door() {
        name = "Door";
        collision = true;
        scale = 1.6;
        solidArea = new Rectangle(0, 0, (int)(48 * scale), (int)(48 * scale));
        images = new BufferedImage[6];
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/doors.png"));
            int count = 0;
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    images[count] = sheet.getSubimage(col * 26, row * 32, 26, 32);
                    count++;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}