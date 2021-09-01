package byog.Core;

import byog.TileEngine.Tileset;
/**
 * the L shaped Hallway, its arguments are startPos, elbowPos, and endPos
 */
public class LHallway extends Hallway {
    public Hallway hallway1;
    public Hallway hallway2;

    public LHallway(Position pos, int direction, int length) {
        super(pos, direction, length);
        Position elbowPos = endPos;
        int direction2;
        if (direction == Game.UP || direction == Game.DOWN) {
            direction2 = RandomUtils.bernoulli(Game.RANDOM) ? Game.LEFT : Game.RIGHT;
        } else {
            direction2 = RandomUtils.bernoulli(Game.RANDOM) ? Game.UP : Game.DOWN;
        }
        int length2 = RandomUtils.uniform(Game.RANDOM, Game.MINEDGELEN, Game.MAXEDGELEN);
        this.direction = direction2;
        endPos = Game.getEndPos(elbowPos, direction2, length2);
        hallway1 = new Hallway(pos, direction, length);
        hallway2 = new Hallway(elbowPos, direction2, length2);
    }

    @Override
    public void render() {
        Game.drawHallway(this.hallway1);
//            System.out.println(hallway2.startPos.x);
//            System.out.println(hallway2.startPos.y);
        hallway2.startPos.shift(Game.oppoDir(direction), 1);
//            System.out.println(hallway2.startPos.x);
//            System.out.println(hallway2.startPos.y);
        Game.drawHallway(this.hallway2);
        Game.setTile(this.hallway2.startPos, Tileset.WALL);
    }

    @Override
    public boolean isValid() {
        return hallway1.isValid() && hallway2.isValid();
    }
}