package comp1140.ass2.ai;

import comp1140.ass2.util.*;

import java.util.LinkedList;

/**
 * Tree of nodes.
 */
public class StateTree extends RoseTree<Node> {
    public StateTree(State data,
                     StateTree parent) {
        super(new Node(0, 0, data), parent, new LinkedList<>());
    }
}
