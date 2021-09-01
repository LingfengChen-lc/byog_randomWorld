package byog.Core;

/**
 * the + shaped hallway
 */
public class CrossHallway extends LHallway {

    public CrossHallway(Position pos, int direction, int length) {
        super(pos, direction, length);
        int length2 = RandomUtils.uniform(Game.RANDOM, Game.MINEDGELEN, Game.MAXEDGELEN);
        int direction2;
        Position startPos;
        if (direction == Game.UP || direction == Game.DOWN) {
            direction2 = Game.RIGHT;
            startPos = new Position(pos.x - length2 / 2, pos.y + length / 2);
        } else {
            direction2 = Game.UP;
            startPos = new Position(pos.x - length / 2, pos.y - length2 / 2);
        }
//            endPos = getEndPos(startPos, direction2, length2);    // set endPos as the cross hallway's endPos
        this.direction = direction2;
        endPos = Game.getEndPos(pos, direction, length);
        hallway1 = new Hallway(pos, direction, length);
        hallway2 = new Hallway(startPos, direction2, length2);
    }
//        @Override
//        public void render() {
//            drawHallway(this.hallway1);
//            drawHallway(this.hallway2);
//        }
//
//        @Override
//        public boolean isValid() {
//            return hallway1.isValid() && hallway2.isValid();
//        }
}