package comp1140.ass2.ai;

import java.util.Set;
import java.util.HashSet;
import comp1140.ass2.Tsuro;
import comp1140.ass2.util.UnorderedPair;

/**
 * Wake tile.
 */
public class Wake extends Tile {
    public final int id;
    public final Set<UnorderedPair<Integer>> exits;

    public Wake(int id, int rot) {
        super(rot);

        this.id = id;
        final String exits = Tsuro.WAKE_TILE_PATHS[id];

        this.exits = new HashSet<>(4);
        for (int i = 0; i < exits.length(); i += 2) {
            UnorderedPair<Integer> e = new UnorderedPair<>(
                Character.getNumericValue(exits.charAt(i)),
                Character.getNumericValue(exits.charAt(i + 1))
            );
            this.exits.add(e);
        }
    }
}
