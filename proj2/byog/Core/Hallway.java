package byog.Core;

/**
 * This Hallway class is designed to avoid building WALLS inside rooms
 */
public class Hallway {
    Position startPos;
    Position endPos;
    int direction;

    public Hallway(Position pos, int direction, int length) {
        this.startPos = pos;
        this.direction = direction;
//            this.length = length;
        endPos = Game.getEndPos(pos, direction, length);
    }

    /**
     * The constructor
     * @param startPos where the hallway starts at
     * @param endPos where the hallway ends at
     */
    public Hallway(Position startPos, Position endPos) {
        this.startPos = startPos;
//            this.direction = direction;
        if (endPos.x == startPos.x) {
            if (endPos.y > startPos.y) {
                direction = Game.UP;
            } else {
                direction = Game.DOWN;
            }
        } else {
            if (endPos.x > startPos.x) {
                direction = Game.RIGHT;
            } else {
                direction = Game.LEFT;
            }
        }
        this.endPos = endPos;
    }

    /**
     * directly call the drawHallway method on this object
     */
    public void render() {
        Game.drawHallway(this);
    }
    /**
     * tells if the hallway is valid or not
     * @return true or false
     */
    public boolean isValid() {
        return startPos.isValid() && endPos.isValid();
    }
}