package comp1140.ass2.ai;

import comp1140.ass2.util.*;

import static comp1140.ass2.ai.Daikaiju.Dir;
import static comp1140.ass2.core.WakeTile.getOppositeExit;

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

    public boolean equalsState(Triple<Integer, Integer, Integer> xye) {
        return this.x == xye.a && this.y == xye.b && this.exit == xye.c;
    }

    public boolean isAtEdge() {
        switch (this.exit) {
            case 0:
            case 1:
                return y == 0;
            case 2:
            case 3:
                return x == 6;
            case 4:
            case 5:
                return y == 6;
            case 6:
            case 7:
                return x == 0;
            default:
                return false;
        }
    }

    public Daikaiju.Dir exitDir() {
        switch (this.exit) {
            case 0:
            case 1:
                return Dir.UP;
            case 2:
            case 3:
                return Dir.LEFT;
            case 4:
            case 5:
                return Dir.DOWN;
            case 6:
            case 7:
                return Dir.RIGHT;
            default:
                return null;
        }
    }
}
