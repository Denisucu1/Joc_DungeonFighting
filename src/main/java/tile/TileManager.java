package tile;

import mygame.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10]; // Putem avea 10 tipuri diferite de tile-uri
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = loadImage("/tiles/grass.png");

            tile[1] = new Tile();
            tile[1].image = loadImage("/tiles/wall.png");
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = loadImage("/tiles/water.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Încearcă să încarce o imagine din classpath (/tiles/...) și, dacă nu există,
     * încearcă din folderul relativ "res/tiles/..." de pe sistemul de fișiere.
     * Returnează null dacă nu a reușește.
     */
    private BufferedImage loadImage(String resourcePath) {
        try {
            // 1) Încearcă classpath
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is != null) {
                try (InputStream in = is) {
                    BufferedImage img = ImageIO.read(in);
                    if (img != null) {
                        System.out.println("Loaded " + resourcePath + " from classpath");
                        return img;
                    }
                }
            } else {
                System.out.println("Not found on classpath: " + resourcePath);
            }

            // 2) Fallback: încearcă în folderul res/ (working dir)
            File f = new File("res" + resourcePath); // resourcePath începe cu '/'
            System.out.println("Trying filesystem path: " + f.getAbsolutePath());
            if (f.exists()) {
                try (InputStream in = new FileInputStream(f)) {
                    BufferedImage img = ImageIO.read(in);
                    if (img != null) {
                        System.out.println("Loaded " + resourcePath + " from file: " + f.getAbsolutePath());
                        return img;
                    }
                }
            } else {
                System.out.println("File does not exist: " + f.getAbsolutePath());
            }

            System.err.println("Resource not found on classpath or filesystem: " + resourcePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadMap() {
        // Aici am putea citi dintr-un fișier .txt, dar pentru început
        // hai să umplem harta cu "0" (iarbă) și marginea cu "1" (perete)
        for (int col = 0; col < gp.maxScreenCol; col++) {
            for (int row = 0; row < gp.maxScreenRow; row++) {
                if (col == 0 || col == gp.maxScreenCol - 1 || row == 0 || row == gp.maxScreenRow - 1) {
                    mapTileNum[col][row] = 1; // Perete pe margini
                } else {
                    mapTileNum[col][row] = 0; // Iarbă în interior
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int tileNum = mapTileNum[col][row];
            if (tile[tileNum] != null && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}