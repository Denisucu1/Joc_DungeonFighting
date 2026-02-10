package mygame;

import object.OBJ_Key;
import java.util.Random;

public class AssetSetter {
    GamePanel gp;
    Random random = new Random();

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // Spawnăm 5 chei în locuri random
        for(int i = 0; i < 5; i++) {
            gp.obj[i] = new OBJ_Key();

            // Generăm coloană și rând între 1 și 48 (ca să evităm pereții de pe margini)
            int worldCol = random.nextInt(48) + 1;
            int worldRow = random.nextInt(48) + 1;

            // Verificăm dacă locul ales este iarbă (tile 0)
            // Dacă e perete sau apă, mai încercăm o dată pentru acest index
            if(gp.tileM.mapTileNum[worldCol][worldRow] != 0) {
                i--;
                continue;
            }

            gp.obj[i].worldX = worldCol * gp.tileSize;
            gp.obj[i].worldY = worldRow * gp.tileSize;
        }
    }
}