package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

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
    static public int ROOMNUM = 45;
    static public int HALLWAYNUM = 44;
    static public final int MAXEDGELEN = 15;
    static public final int MINEDGELEN = 4;
    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    private static final int SEED = 4321;   // TOD: get user input/keyboard input
    private static final Random RANDOM = new Random(SEED);
//    List<Room> ROOMS = new ArrayList<>();
//    List<Hallway> HALLWAYS = new ArrayList<>();

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
        System.out.println("Input: " + input);
        ter.initialize(WIDTH, HEIGHT);
//        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWorldFrame[i][j] = Tileset.NOTHING;
            }
        }
//        int roomNum = RandomUtils.uniform(RANDOM, 1, 5);
//        int hallwayNum = RandomUtils.uniform(RANDOM, roomNum - 1, 10);

        Room currentRoom = addRandomRoom();
        currentRoom.drawRoom();
        while (HALLWAYNUM > 0 || ROOMNUM > 0) {
            Position edgePos = currentRoom.pickEdge();
            Hallway hallway = currentRoom.spawnHallway(edgePos);
            if (!hallway.isValid()) {
                continue;
            }
            drawHallway(hallway.startPos, hallway.endPos, hallway.direction);
            int width = RandomUtils.uniform(RANDOM, MINEDGELEN, MAXEDGELEN);
            int height = RandomUtils.uniform(RANDOM, MINEDGELEN, MAXEDGELEN);
            int offsetX = RandomUtils.uniform(RANDOM, 1, width - 1);
            int offsetY = RandomUtils.uniform(RANDOM, 1, height - 1);
            Position pos = switch (hallway.direction) {
                case 0:
                    yield new Position(hallway.endPos.x - offsetX, hallway.endPos.y);
                case 1:
                    yield new Position(hallway.endPos.x, hallway.endPos.y - offsetY);
                case 2:
                    yield new Position(hallway.endPos.x - offsetX, hallway.endPos.y - height + 1);
                case 3:
                    yield new Position(hallway.endPos.x - width + 1, hallway.endPos.y - offsetY);
                default: throw new IllegalArgumentException("unexpected argument: " + hallway.direction);
            };
            Room tempRoom = addRoom(pos, width, height);
            if (!tempRoom.isValid()) {
                finalWorldFrame[hallway.endPos.x][hallway.endPos.y] = Tileset.WALL;     // enclose hallway near edge
                continue;
            }
            currentRoom = tempRoom;
//            ROOMS.add(currentRoom);
            currentRoom.drawRoom();
        }

//        for (int i = 0; i < roomNum; i++) {
//            ROOMS.get(i).drawRoom();
//        }
//        for (Hallway h : HALLWAYS) {
//            setTile(h.startPos, Tileset.SAND);
//            setTile(h.endPos, Tileset.FLOWER);
//        }


        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public void setTile(Position pos, TETile tile) {
        finalWorldFrame[pos.x][pos.y] = tile;
    }


    /**
     * The room class defines a self-enclosed space which ensures to have at least one hallway connecting to other rooms
     */
    public class Room {
        public Position pos;
        public int width, height;
        public int direction = -1;

        public Room (Position pos, int width, int height) {
            this.pos = pos;
            this.width = width;
            this.height = height;
        }

        private void drawRoom() {
            int x = pos.x, y = pos.y;
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    if (i == x || j == y || i == x + width - 1 || j == y + height - 1) {
                        if (finalWorldFrame[i][j] == Tileset.FLOOR) {
                            continue;
                        } else {
                            finalWorldFrame[i][j] = Tileset.WALL;
                        }
                    } else {
                        finalWorldFrame[i][j] = Tileset.FLOOR;
                    }
                }
            }
            ROOMNUM--;
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
         * randomly pick an edge of the room and return the position, no corner should be picked by this function, set the direction variable
         * @return edge point position
         */
        private Position pickEdge() {
            int i = 0, j = 0;
            int x = pos.x, y = pos.y;
            if (RandomUtils.bernoulli(RANDOM)) {   // hallway on the left/right side
                i = RandomUtils.bernoulli(RANDOM) ? x : x + width - 1;
                j = RandomUtils.uniform(RANDOM, y + 1, y + height - 2);   // -2 because do not want edge
                direction = i == x ? 3 : 1;
            } else {  // hallway on the top or bottom side
                i = RandomUtils.uniform(RANDOM, x + 1, x + width - 2);
                j = RandomUtils.bernoulli(RANDOM) ? y : y + height - 1;
                direction = j == y ? 2 : 0;
            }
            return new Position(i, j);
        }

        /**
         * spawn a hallway connect with this room with specified direction and hallway length
         * @param edgePos is the position where the hallway starts
         * @return the hallway object
         */
        private Hallway spawnHallway(Position edgePos) {
            int len = RandomUtils.uniform(RANDOM, MINEDGELEN, MAXEDGELEN);         // length of hallway
            return new Hallway(edgePos, direction, len);       // the hallway preserves the direction variable of the room where the hallway is spawned from
        }
    }

    /**
     * add a random room in the world and guarantee the room is a valid room
     * @return the room object
     */
    public Room addRandomRoom() {
        int i = RandomUtils.uniform(RANDOM, 0, WIDTH - 8);
        int j = RandomUtils.uniform(RANDOM, 0, HEIGHT - 8);
        int width = RandomUtils.uniform(RANDOM, 4, 8);
        int height = RandomUtils.uniform(RANDOM, 4, 8);
        return new Room(new Position(i, j), width, height);
    }

    /**
     * add a random room in the world x index i and y index j, with random width and length
     * @param pos: position to add a room
     * @return room object
     */
    public Room addRoom(Position pos, int width, int height) {
        return new Room(pos, width, height);
    }

    /**
     * This Hallway class is designed to avoid building WALLS inside rooms
     */
    public class Hallway {
        Position startPos;
        Position endPos;
        int direction, length;

        public Hallway() {
            startPos = null;
            endPos = null;
            direction = -1;
            length = 0;
        }

        public Hallway(Position pos, int direction, int length) {
            this.startPos = pos;
            this.direction = direction;
            this.length = length;
            endPos = getEndPos(pos, direction, length);
        }
        /**
         * tells if the hallway is valid or not
         * @return true or false
         */
        public boolean isValid() {
            return startPos.isValid() && endPos.isValid();
        }
    }

    public class LHallway extends Hallway{
        private Position elbowPos;
        int direction2;
        int length2;
        public LHallway(Position pos, int direction, int length) {
            super(pos, direction, length);
            elbowPos = endPos;
            if (direction == UP || direction == DOWN) {
                direction2 = RandomUtils.bernoulli(RANDOM) ? LEFT : RIGHT;
            } else {
                direction2 = RandomUtils.bernoulli(RANDOM) ? UP : DOWN;
            }
            length2 = RandomUtils.uniform(RANDOM, MINEDGELEN, MAXEDGELEN);
            endPos = getEndPos(elbowPos, direction2, length2);
        }
    }

    public Position getEndPos(Position pos, int direction, int length) {
        Position endPos = switch (direction) {
            case UP -> new Position(pos.x, pos.y + length);
            case RIGHT -> new Position(pos.x + length, pos.y);
            case DOWN -> new Position(pos.x, pos.y - length);
            case LEFT -> new Position(pos.x - length, pos.y);
            default -> throw new IllegalArgumentException("unexpected direction: " + direction);
        };
        return endPos;
    }


    /**
     * add FLOOR Tile to the hallway, and enclose the edge with WALL tile if there is space
     */
    public void drawHallway(Position startPos, Position endPos, int direction) {
//        Position startPos = hallway.startPos;
//        Position endPos = hallway.endPos;
//        int direction = hallway.direction;
        if (startPos.x == endPos.x) {   // vertical hallway
            int x = startPos.x;
            int yStart = direction == UP ? startPos.y : endPos.y;
            int yEnd = direction == UP ? endPos.y : startPos.y;
            for (int j = Math.max(0, yStart); j <= yEnd; j++) {
                finalWorldFrame[x][j] = Tileset.FLOOR;
                if (x - 1 >= 0 && finalWorldFrame[x - 1][j] != Tileset.FLOOR) {
                    finalWorldFrame[x - 1][j] = Tileset.WALL;
                }
                if (x + 1 < WIDTH && finalWorldFrame[x + 1][j] != Tileset.FLOOR) {
                    finalWorldFrame[x + 1][j] = Tileset.WALL;
                }
            }
        } else if (startPos.y == endPos.y){  // horizontal hallway
            int y = startPos.y;
            int xStart = direction == RIGHT ? startPos.x : endPos.x;
            int xEnd = direction == RIGHT ? endPos.x : startPos.x;
            for (int i = Math.max(0, xStart); i <= xEnd; i++) {
                finalWorldFrame[i][y] = Tileset.FLOOR;
                if (y - 1 >= 0  && finalWorldFrame[i][y - 1] != Tileset.FLOOR) {
                    finalWorldFrame[i][y - 1] = Tileset.WALL;
                }
                if (y + 1 < HEIGHT   && finalWorldFrame[i][y + 1] != Tileset.FLOOR) {
                    finalWorldFrame[i][y + 1] = Tileset.WALL;
                }
            }
        }
        HALLWAYNUM--;
    }
}
