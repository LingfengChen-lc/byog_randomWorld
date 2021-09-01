package byog.Core;

/**
 * Position on the map which contains a x coordinate and y coordinate
 */
public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * shift the Position along the direction by length n
     * @param direction direction to shift the Position
     * @param n number of distance from previous position
     */
    public void shift(int direction, int n) {
        switch (direction) {
            case Game.UP:
                y += n;
                break;
            case Game.DOWN:
                y -= n;
                break;
            case Game.LEFT:
                x -= n;
                break;
            case Game.RIGHT:
                x += n;
                break;
        }
    }

    public boolean isValid() {
        return 0 <= x && x < Game.WIDTH && 0 <= y && y < Game.HEIGHT;
    }
}
