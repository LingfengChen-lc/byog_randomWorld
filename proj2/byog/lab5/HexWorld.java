package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    public static int SIZE;
    public static TETile[][] TILES;
    TETile currentTile;
    private static final long SEED = 45433;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Position on the map which contains a x coordinate and y coordinate
     */
    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public HexWorld(int size) {
        SIZE = size;
        currentTile = Tileset.FLOWER;
        TILES = new TETile[WIDTH][HEIGHT];
        fill();
    }

//    public void drawHex(Position pos) {
//        TERenderer render = new TERenderer();
//
//        render.initialize(WIDTH, HEIGHT);
//
//        addVerticalHex(pos, N);
//
//        render.renderFrame(TILES);
//    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.MOUNTAIN;
            case 2: return Tileset.WATER;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.SAND;
            default: return Tileset.FLOWER;
        }
    }

    /**
     * Fill the initial TILES with NOTHING tiles
     */
    private void fill() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                TILES[i][j] = Tileset.NOTHING;
            }
        }
    }


    /**
     * Create a single Hexagon in specific x and y position
     * @param pos contains the position that specifies the x and y coordinate of the Hexagon
     */
    public void addHexdecimal(Position pos, TETile tile) {
        int offsetX = pos.x, offsetY = pos.y;
        Random rand = new Random(1000);
        for (int x = 0; x <= 3 * SIZE - 3; x++) {
            for (int y = 0; y < 2 * SIZE; y++) {
                if (insideHex(x, y) && bounded(x + offsetX, y + offsetY)) {
                    TILES[x + offsetX][y + offsetY] = tile;
//                    tile = TETile.colorVariant(tile, 100, 100, 100, rand);
                }
            }
        }
        currentTile = randomTile();
    }

    /**
     * return the upperRight position
     * @param pos existing hex position
     * @return new hex position
     */
    public static Position upperRight(Position pos) {
        return new Position(pos.x + 2 * SIZE - 2, pos.y + SIZE);
    }

    /**
     * return the upperLeft position
     * @param pos existing hex position
     * @return new hex position
     */
    public static Position upperLeft(Position pos) {
        return new Position(pos.x - 2 * SIZE + 2, pos.y + SIZE);
    }

    /**
     * The method returns the Position to add a adjacent Hexdecimal to the existing one
     * @param pos position of the existing Hexdecimal
     * @return return all the positions that could be add an hexdecimal
     */
    public Position[] HexAdjacentPos(Position pos) {
        Position[] positions = new Position[6];
        positions[0] = new Position(pos.x - 2 * SIZE + 2, pos.y - SIZE);  // bottom left
        positions[1] = new Position(pos.x - 2 * SIZE + 2, pos.y + SIZE);  // upper left
        positions[2] = new Position(pos.x + 2 * SIZE - 2, pos.y - SIZE);  // bottom right
        positions[3] = new Position(pos.x + 2 * SIZE - 2, pos.y + SIZE);  // upper right
        positions[4] = new Position(pos.x, pos.y + 2 * SIZE); // upper
        positions[5] = new Position(pos.x, pos.y - 2 * SIZE); // bottom
        return positions;
    }

    /**
     * From the top Hex, vertically add new N hexes to the bottom of the existing hex
     * @param pos the position to add vertical Hex
     * @param N number to add
     */
    public void addVerticalHex(Position pos, int N) {
        for (int i = 0; i < N; i++) {
            Position newPos = new Position(pos.x, pos.y + 2 * i * SIZE);
            addHexdecimal(newPos, currentTile);
        }
    }

    private boolean insideHex(int x, int y) {
        if (x < SIZE - 1) {
            if (y < SIZE - 1) {  // test bottom left edge
                return y >= bottom_left_edge_func(x);
            } else if (y > SIZE) {  // test upper left edge
                return y <= upper_left_edge_func(x);
            }
        } else if (x > 2 * SIZE - 2) {
            if (y < SIZE - 1) {
                return y >= bottom_right_edge_func(x);
            } else if (y > SIZE) {
                return y <= upper_right_edge_func(x);
            }
        }
        return true;
    }

    /**
     * the function of the bottom left edge
     * @param x the x-axis of the new point
     * @return the y value on the edge
     */
    private int bottom_left_edge_func(int x) {
        return  -x  + SIZE - 1;
    }
    /**
     * the function of the bottom right edge
     * @param x the x-axis of the new point
     * @return the y value on the edge
     */
    private int bottom_right_edge_func(int x) {
        return x - 2 * SIZE + 2;
    }
    /**
     * the function of the upper left edge
     * @param x the x-axis of the new point
     * @return the y value on the edge
     */
    private int upper_left_edge_func(int x) {
        return x + SIZE;
    }
    /**
     * the function of the upper right edge
     * @param x the x-axis of the new point
     * @return the y value on the edge
     */
    private double upper_right_edge_func(int x) {
        return -x + (4 * SIZE - 3);
    }

    /**
     * check whether the x and y axis are bounded in the window
     * @param x x axis
     * @param y y axis
     * @return true if bounded in the window
     */
    private boolean bounded(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public static void main(String[] args) {
        HexWorld myHex = new HexWorld(5);

        TERenderer render = new TERenderer();

        render.initialize(WIDTH, HEIGHT);

        Position initPos = new Position(18, 0);

        myHex.addVerticalHex(initPos, 5);

        myHex.addVerticalHex(upperLeft(initPos), 4);

        myHex.addVerticalHex(upperRight(initPos), 4);

        myHex.addVerticalHex(upperLeft(upperLeft(initPos)), 3);

        myHex.addVerticalHex(upperRight(upperRight(initPos)), 3);

        render.renderFrame(TILES);


//        myHex.drawHex(new Position(18, 0));
//        Position[] positions = HexAdjacentPos(myHex);
    }
}
