package comp1140.ass2.ai;

public class Daikaiju extends Tile {
    enum Col { B, G }
    enum Dir { ROT, UP, LEFT, DOWN, RIGHT;
        private static final Dir[] vals = values();

        private Dir next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Dir applyRot(int rot) {
            if (this == ROT) {
                return this;
            }
            else {
                Dir d = this;
                for (int i = 0; i < rot; i++) {
                    if (d == ROT) {
                        i--;
                    }
                    d = d.next();
                }
                return d;
            }
        }
    }
    final Dir[][] movementTemplate = {
        {Dir.ROT, Dir.UP, Dir.LEFT, Dir.DOWN, Dir.RIGHT},
        {Dir.RIGHT, Dir.ROT, Dir.UP, Dir.LEFT, Dir.DOWN},
        {Dir.DOWN, Dir.RIGHT, Dir.ROT, Dir.UP, Dir.LEFT},
        {Dir.LEFT, Dir.DOWN, Dir.RIGHT, Dir.ROT, Dir.UP},
        {Dir.UP, Dir.LEFT, Dir.DOWN, Dir.RIGHT, Dir.ROT}
    };

    public final Col colour;
    public final int type;
    public final Dir[] movements;

    public Daikaiju(Col colour, int type, int rot) {
        super(rot);

        this.colour = colour;
        this.type = type;
        this.movements = movementTemplate[type - 1];
    }
}
