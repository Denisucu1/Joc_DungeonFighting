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
        solidArea = new Rectangle(-5, -5, (int)(48 * scale)+10, (int)(48 * scale)+10);

        // MODIFICARE: Încărcăm doar 3 imagini (un singur rând)
        images = new BufferedImage[3];

        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/doors.png"));

            // Încărcăm doar cadrele de pe primul rând (row = 0)
            for (int col = 0; col < 3; col++) {
                images[col] = sheet.getSubimage(col * 26, 0, 26, 32);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}