package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.ArrayList;

import java.util.Random;



public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    static public final int UP = 0;
    static public final int RIGHT = 1;
    static public final int DOWN = 2;
    static public final int LEFT = 3;
    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    private static final int SEED = 23452;   // TOD: get user input/keyboard input
    private static final Random RANDOM = new Random(SEED);
    List<Room> ROOMS = new ArrayList<>();
    List<Hallway> HALLWAYS = new ArrayList<>();

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

        public boolean isValid() {
            return 0 <= x && x < WIDTH && 0 <= y && y < HEIGHT;
        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
//        if (input.length())
//        if (input.charAt(0) == '')
        ter.initialize(WIDTH, HEIGHT);
//        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWorldFrame[i][j] = Tileset.NOTHING;
            }
        }
        int roomNum = RandomUtils.uniform(RANDOM, 1, 10);
        int hallwayNum = RandomUtils.uniform(RANDOM, roomNum - 1, 20);


        Room currentRoom = drawRandomRoom();
        while (ROOMS.size() < roomNum || HALLWAYS.size() < hallwayNum) {
            Position edgePos = currentRoom.pickEdge();
            int[] hallwayEndIndex = currentRoom.spawnHallway(edgePos);
            currentRoom = drawRandomRoom(hallwayEndIndex[0], hallwayEndIndex[1]);
        }

        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }


    /**
     * The room class defines a self-enclosed space which ensures to have at least one hallway connecting to other rooms
     */
    public class Room {
        public Position pos;
        public int width, height;
        public int direction = 0;

        public Room (Position pos, int width, int height) {
            this.pos = pos;
            this.width = width;
            this.height = height;
//            drawRoom();
        }

        private void drawRoom() {
            int x = pos.x, y = pos.y;
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    if (i == x || j == y || i == x + width - 1 || j == y + height - 1) {
                        if (finalWorldFrame[i][j] == Tileset.WALL) {
                            finalWorldFrame[i][j] = Tileset.FLOOR;;
                        } else if (finalWorldFrame[i][j] != Tileset.FLOOR) {
                            finalWorldFrame[i][j] = Tileset.WALL;
                        }
                    } else {
                        if (finalWorldFrame[i][j] != Tileset.WALL) {
                            finalWorldFrame[i][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }

        /**
         * tells if this room is valid (bounded by WIDTH and HEIGHT)
         * @return boolean valid or not valid
         */
        private boolean isValid() {
            return pos.isValid() && new Position(pos.x + width, pos.y + height).isValid();
        }

        /**
         * indicate the position of the room
         * @return 0: upper left, 1: upper right, 2: lower right, 3: lower left
         */
        public int roomPosition() {
            int x = pos.x, y = pos.y;
            int i = x + width / 2, j = y + height / 2;
            if (i <= WIDTH / 4) {
                if (j >= HEIGHT / 4) {
                    return 0;
                } else {
                    return 3;
                }
            } else if (i >= WIDTH * 3 / 4){
                if (j >= HEIGHT / 4) {
                    return 1;
                } else {
                    return 2;
                }
            }
            return 4;
        }

        /**
         * randomly pick an edge of the room and return the position, set the direction variable
         * @return edge point position
         */
        private Position pickEdge() {
            int i = 0, j = 0;
            int x = pos.x, y = pos.y;
            if (RandomUtils.bernoulli(RANDOM)) {   // hallway on the left/right side
                i = RandomUtils.bernoulli(RANDOM) ? x : x + width;
                j = RandomUtils.uniform(RANDOM, y, y + height);
                direction = i == x ? 3 : 1;
            } else {  // hallway on the top or bottom side
                i = RandomUtils.uniform(RANDOM, x, x + width);
                j = RandomUtils.bernoulli(RANDOM) ? y : y + height;
                direction = j == y ? 2 : 0;
            }
            assert(x <= i && i <= x + width);
            assert(y <= j && j <= y + height);
            return new Position(i, j);
        }

        /**
         * spawn a hallway connect with this room with specified direction and hallway length
         * @param edgePos is the position where the hallway starts
         * @return the end index of the hallway
         */
        private int[] spawnHallway(Position edgePos) {
            int direction = this.direction;
            int len = RandomUtils.uniform(RANDOM, 4, 10);         // length of hallway
//            new Hallway(edgePos, )
            return null;

            // ensures if room is on the corner+
            // param direction 0: up, 1: right, 2: down, 3: left
//            int direction = switch (roomPosition()) {
//                case 0: yield RandomUtils.bernoulli(RANDOM) ? 1 : 2;
//                case 1: yield RandomUtils.bernoulli(RANDOM) ? 2 : 3;
//                case 2: yield RandomUtils.bernoulli(RANDOM) ? 0 : 3;
//                case 3: yield RandomUtils.bernoulli(RANDOM) ? 0 : 1;
//                case 4: yield RandomUtils.uniform(RANDOM, 0, 4);
//                default: throw new IllegalArgumentException("Unexpected argument" + roomPosition());
//            };

//            switch (direction) {
//                case 0:
//                    Hallway h = new Hallway(new Position(i, j), 3, len);
//                    if (h.isValid()) {
//                        HALLWAYS.add(h);
//                        return new int[]{i, j + len};
//                    }
//                    HALLWAYS.add(new Hallway(new Position(i, j), 3, len));
//                    return new int[]{i, j + len};
//                case 1:
//                    HALLWAYS.add(new Hallway(new Position(i, j), len, 3));
//                    return new int[]{i + len, j};
//                case 2:
//                    HALLWAYS.add(new Hallway(new Position(i, j - len), 3, len));
//                    return new int[]{i, j - len};
//                case 3:
//                    HALLWAYS.add(new Hallway(new Position(i - len, j), len, 3));
//                    return new int[]{i - len, j};
//                default: return null;
//            }
        }
    }

    /**
     * draw a random room in the world and return the room
     * @return the room object if valid, null if not valid
     */
    public Room drawRandomRoom() {
        int i = RandomUtils.uniform(RANDOM, 0, WIDTH);
        int j = RandomUtils.uniform(RANDOM, 0, HEIGHT);
        int width = RandomUtils.uniform(RANDOM, 3, 10);
        int height = RandomUtils.uniform(RANDOM, 3, 10);
        Room room = new Room(new Position(i, j), width, height);
        if (room.isValid()) {
            ROOMS.add(room);
            return room;
        }
        return null;
    }

    /**
     * draw a random room in the world x index i and y index j, with random width and length
     * @param i: x axis
     * @param j: y axis
     * @return room object if it is valid, null if it is not valid
     */
    public Room drawRandomRoom(int i, int j) {
        int width = RandomUtils.uniform(RANDOM, 3, WIDTH - i);
        int height = RandomUtils.uniform(RANDOM, 3, HEIGHT - j);
//        int offsetX = RandomUtils.uniform(RANDOM, 0, Math.min(width, i));  // I > offsetX
//        int offsetY = RandomUtils.uniform(RANDOM, 0, Math.min(height, j));  // j > offsetY
//        ROOMS.add(new Room(i - offsetX, j - offsetY, width, height));
        Room room = new Room(new Position(i, j), width, height);
        if (room.isValid()) {
            ROOMS.add(room);
            return room;
        }
        return null;
    }

    /**
     * This Hallway class is designed to avoid building WALLS inside rooms
     */
    public class Hallway {
        Position startPos;
        Position endPos;
//        int width, height;
        int direction, length;

        public Hallway(Position pos, int direction, int length) {
            this.startPos = pos;
            this.direction = direction;
            this.length = length;
            endPos = switch (direction) {
                case UP -> new Position(pos.x, pos.y + length);
                case RIGHT -> new Position(pos.x + length, pos.y);
                case DOWN -> new Position(pos.x, pos.y - length);
                case LEFT -> new Position(pos.x - length, pos.y);
                default -> throw new IllegalArgumentException("unexpected direction: " + direction);
            };
//            addWay();
        }

        /**
         * add FLOOR Tile to the hallway, and enclose the edge with WALL tile if there is space
         */
        private void addHallway() {
            if (startPos.x == startPos.y) {
                int x = startPos.x;
                int yStart = direction == UP ? startPos.y : endPos.y;
                int yEnd = direction == UP ? endPos.y : startPos.y;
                for (int j = yStart; j < yEnd; j++) {
                    finalWorldFrame[x][j] = Tileset.FLOOR;
                    if (x - 1 >= 0) {
                        finalWorldFrame[x - 1][j] = Tileset.WALL;
                    }
                    if (x + 1 < WIDTH) {
                        finalWorldFrame[x + 1][j] = Tileset.WALL;
                    }
                }
            } else if (startPos.y == endPos.y){
                int y = startPos.y;
                int xStart = direction == RIGHT ? startPos.y : endPos.y;
                int xEnd = direction == RIGHT ? endPos.y : startPos.y;
                for (int i = xStart; i < xEnd; i++) {
                    finalWorldFrame[i][y] = Tileset.FLOOR;
                    if (y - 1 >= 0) {
                        finalWorldFrame[i][y - 1] = Tileset.WALL;
                    }
                    if (y + 1 < HEIGHT) {
                        finalWorldFrame[i][y + 1] = Tileset.WALL;
                    }
                }
            }
        }




//            for (int i = x; i < endPos.x; i++) {
//                for (int j = y; j < endPos.y; j++) {
//                    if (direction == UP) {
//                        if (i == x + 1) {
//                            finalWorldFrame[i][j] = finalWorldFrame[i][j] == Tileset.NOTHING && (j == y || j == y + height - 1) ? Tileset.WALL : Tileset.FLOOR;
//                        } else if (finalWorldFrame[i][j] != Tileset.FLOOR) {
//                            finalWorldFrame[i][j] = Tileset.WALL;
//                        }
//                    } else if (height == 3) {
//                        if (j == y + 1) {
//                            finalWorldFrame[i][j] = finalWorldFrame[i][j] == Tileset.NOTHING && (i == x || j == x + width - 1) ? Tileset.WALL : Tileset.FLOOR;
//                        } else if (finalWorldFrame[i][j] != Tileset.FLOOR) {
//                            finalWorldFrame[i][j] = Tileset.WALL;
//                        }
//                    }
//                }
//            }



        /**
         * tells if the hallway is valid or not
         * @return true or false
         */
        public boolean isValid() {
            return startPos.isValid() && endPos.isValid();
        }
    }
}
