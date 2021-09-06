package byog.TileEngine;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the player or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setFont(new Font("Monaco", Font.BOLD, TILE_SIZE - 2));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        StdDraw.show();
    }

    /**
     * return a user input seed if press S, return a loaded seed if press L, safely exit program if press Q
     * @return seed
     */
    public long renderMenu() {
        StdDraw.clear();
        StdDraw.clear(new Color(0,0,0));
        StdDraw.setPenColor(new Color(7, 77, 199));
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(width / 2.0, height / 2.0 + 10, "Welcome to Liam's Game!");
        StdDraw.text(width / 2.0, height / 2.0, "Press A Bottom");
        StdDraw.text(width / 2.0, height / 2.0 - 10, "Start Game (S)");
        StdDraw.text(width / 2.0, height / 2.0 - 20, "Load Game (L)");
        StdDraw.text(width / 2.0, height / 2.0 - 30, "Quit (Q)");
        StdDraw.show();
        String seed = "";
        boolean inMenu = true;
        while (inMenu) {
            String action = solicitInput(1);
            switch (action) {
                case "q":
                case "Q":
                    System.exit(0);
                case "l":
                case "L":
                    // TODO: load the game
                    break;
                case "s":
                case "S":
                    seed = solicitSeed();
                    inMenu = false;
                    break;
                default: continue;
            }
        }

        return Long.valueOf(seed);
    }

    /**
     * solicit input from user with length n
     * @param n length of input
     * @return the input string
     */
    public String solicitInput(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                n--;
                sb.append(StdDraw.nextKeyTyped());
            }
        }
        return sb.toString();
    }

    /**
     * solicit an array of numerical digits with unknown length from user and range[0, Long.MAX_VALUE];
     * @return the input numerical seed
     */
    public String solicitSeed() {
        StringBuilder sb = new StringBuilder();
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.text(width / 2.0, height / 2.0 + 10.0, "Type Away!");
        StdDraw.show();
        while (sb.toString().equals("") || (Long.valueOf(sb.toString())) <= Long.MAX_VALUE) {
            if (StdDraw.hasNextKeyTyped()) {
                char nextChar = StdDraw.nextKeyTyped();
                if (nextChar != '\n') {
                    sb.append(nextChar);
//                    try (Long.valueOf(sb.toString())) {
//                        catch ()
//                    }
                    StdDraw.clear(new Color(0, 0, 0));
                    StdDraw.text(width / 2.0, height / 2.0 + 10.0, "Type Away!");
                    StdDraw.text(width / 2.0, height / 2.0, sb.toString());
                    StdDraw.show();
                } else {
                    break;
                }
            }
        }
//        System.out.println("im out");
        return sb.toString();
    }

    /**
     * draws one frame with argument string
     * @param s string to draw in the center of the screen
     */
    public void drawFrame(String s) {
        drawFrame(width /  2.0, height / 2.0, s);
    }

    /**
     * draws one frame with argument string
     * @param s string to draw in the center of the screen
     */
    public void drawFrame(double x, double y, String s) {
        StdDraw.clear(Color.black);
        StdDraw.text(x, y, s);
        StdDraw.show();
    }
}
