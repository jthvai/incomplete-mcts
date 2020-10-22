package comp1140.ass2.ai;

/**
 * Generic tile.
 */
public class Tile {
    public int rot;

    public Tile(int rot) {
        this.rot = rot;
    }

    /**
     * Clone this.
     *
     * @return Clone of this
     * @throws CloneNotSupportedException Object cannot be cloned
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
