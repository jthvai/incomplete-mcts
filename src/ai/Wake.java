package mcts.ai;

import java.util.Set;
import java.util.HashSet;
import mcts.util.UnorderedPair;

/**
 * Wake tile.
 */
public class Wake extends Tile {
    public final int id;
    public final Set<UnorderedPair<Integer>> exits;

    public Wake(int id, int rot) {
        super(rot);

        this.id = id;
        final String exits = this.WAKE_TILE_PATHS[id];

        this.exits = new HashSet<>(4);
        for (int i = 0; i < exits.length(); i += 2) {
            UnorderedPair<Integer> e = new UnorderedPair<>(
                Character.getNumericValue(exits.charAt(i)),
                Character.getNumericValue(exits.charAt(i + 1))
            );
            this.exits.add(e);
        }
    }

    /**
     * Compute the opposite exit.
     *
     * @param e Exit to evaluate opposite of
     * @return Opposite exit of e
     */
    public static int oppositeExit(int e){
        switch (e) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 2:
                return 7;
            case 3:
                return 6;
            case 4:
                return 1;
            case 5:
                return 0;
            case 6:
                return 3;
            case 7:
                return 2;
            default:
                return -1;
        }
    }

    /**
     * A lookup table for converting the ID names of wake tiles into their corresponding wakes.
     */
    private static String[] WAKE_TILE_PATHS = new String[]{
        "01234567", // 00
        "01234657", // 01
        "02153746", // 02
        "02163547", // 03
        "02173645", // 04
        "02173645", // 05
        "03124567", // 06
        "03124756", // 07
        "03152467", // 08
        "03152467", // 09
        "03152467", // 10
        "03152467", // 11
        "03162547", // 12
        "04123756", // 13
        "04123756", // 14
        "04132567", // 15
        "04152367", // 16
        "04152637", // 17
        "04162537", // 18
        "04162537", // 19
        "04172635", // 20
        "04172635", // 21
        "04172635", // 22
        "05132647", // 23
        "05142367", // 24
        "05142367", // 25
        "05142637", // 26
        "05142637", // 27
        "05142736", // 28
        "05162347", // 29
        "05162734", // 30
        "05162734", // 31
        "05172346", // 32
        "05172346", // 33
        "05172436", // 34
        "06123547", // 35
        "06123547", // 36
        "06123745", // 37
        "06123745", // 38
        "06132457", // 39
        "06132457", // 40
        "06142537", // 41
        "06142537", // 42
        "06172435", // 43
        "06172435", // 44
        "06172534", // 45
        "06172534", // 46
        "07123456", // 47
        "07123456", // 48
        "07132456", // 49
        "07132645", // 50
        "07142356", // 51
        "07142635", // 52
        "07142635", // 53
        "07152436", // 54
        "07152436" // 55
    };
}
