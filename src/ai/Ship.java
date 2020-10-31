// SPDX-License-Identifier: CC0-1.0
package mcts.ai;

import mcts.util.*;

import static mcts.ai.Daikaiju.Dir;

/**
 * Ship, representing a player.
 */
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

    /**
     * Whether this is in the location indicated.
     *
     * @param xye Location to compare against
     * @return True if this is in the same location as xye
     */
    public boolean equalsState(Triple<Integer, Integer, Integer> xye) {
        return this.x == xye.a && this.y == xye.b && this.exit == xye.c;
    }

    /**
     * Whether this is facing the edge of the board.
     *
     * @return True if this is at the edge of the board
     */
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

    /**
     * Direction that this is facing out of its current wake tile.
     *
     * @return Direction of this's current exit
     */
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
