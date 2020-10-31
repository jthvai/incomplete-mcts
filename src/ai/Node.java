package comp1140.ass2.ai;

import comp1140.ass2.util.MutableTriple;

/**
 * Node of visits, times won from this node, and state.
 */
public class Node extends MutableTriple<Integer, Integer, State> {
    public Node(Integer visits, Integer wins, State state) {
        super(visits, wins, state);
    }
}
