// SPDX-License-Identifier: CC0-1.0
package mcts.ai;

import mcts.util.MutableTriple;

/**
 * Node of visits, times won from this node, and state.
 */
public class Node extends MutableTriple<Integer, Integer, State> {
    public Node(Integer visits, Integer wins, State state) {
        super(visits, wins, state);
    }
}
