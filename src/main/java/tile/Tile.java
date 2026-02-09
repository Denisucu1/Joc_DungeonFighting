package tile;

import java.awt.image.BufferedImage;

public class Tile {
    // Image for this tile
    public BufferedImage image;

    // If true, entities cannot walk through this tile
    public boolean collision = false;

    // Clasa Tile pentru a fi folosită de TileManager și altele.
}
