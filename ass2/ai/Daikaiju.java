package comp1140.ass2.ai;

public class Daikaiju extends Tile {
    enum Col { B, G }
    enum Dir { ROT, UP, LEFT, DOWN, RIGHT }
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
