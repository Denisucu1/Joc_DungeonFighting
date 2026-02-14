package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class OBJ_Key extends SuperObject {
    public OBJ_Key() {
        name = "Key";

        // REPARAT: Mai întâi construim raftul (array-ul) cu 1 loc
        images = new BufferedImage[1];

        try {
            // Abia acum putem pune ceva la indexul 0
            images[0] = ImageIO.read(getClass().getResourceAsStream("/objects/key_big.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}