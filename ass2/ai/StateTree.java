package comp1140.ass2.ai;

import comp1140.ass2.util.Pair;
import comp1140.ass2.util.RoseTree;

import java.util.LinkedList;

/**
 * Tree of states, and UCT values.
 */
public class StateTree extends RoseTree<Pair<Double, State>> {
    public StateTree(Double mcVal, State data,
                     RoseTree<Pair<Double, State>> parent,
                     LinkedList<RoseTree<Pair<Double, State>>> children) {
        super(new Pair<Double, State>(mcVal, data), parent, children);
    }
}
