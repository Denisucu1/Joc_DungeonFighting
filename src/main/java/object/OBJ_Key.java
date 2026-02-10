package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject {
    public OBJ_Key() {
        name = "Key";
        try {
            // Asigură-te că ai o imagine key.png în src/main/resources/objects/
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key_big.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}