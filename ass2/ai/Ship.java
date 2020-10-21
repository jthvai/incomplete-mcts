package comp1140.ass2.ai;

public class Ship {
    public final char colour;
    public int x;
    public int y;
    public int exit;
    public boolean me;

    public Ship(char colour, int x, int y, int exit, boolean me) {
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.exit = exit;
        this.me = me;
    }
}
