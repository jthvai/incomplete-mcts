package comp1140.ass2.ai;

import comp1140.ass2.util.*;

import java.util.Random;

/**
 * Monte-Carlo Tree Search for Tsuro (Unfinished).
 */
public class MCTS {
    /**
     * Given the current game state, and three wake tiles in the current player's hand, generate a
     * valid wake tile placement for the current player using Monte-Carlo Tree Search. Assumes that
     * daikaijus have already been moved for this turn.
     *
     * @param state State of the board, daikaijus and ships
     * @param hand Hand of three wake tiles
     * @param shipColour Colour of the ship owned by the player making a move
     * @return String representing a wake tile placement
     */
    public static String generateAction(String[] state, String hand, char shipColour) throws
        CloneNotSupportedException {
        long end = System.currentTimeMillis() + 450; // Cap: 500ms

        State init = new State(state, hand, shipColour);
        Pair<Integer, Integer> xy = init.myAdjacency();

        StateTree root = new StateTree(init, null);
        State clone = (State) init.clone();
        for (Wake w : init.hand) {
            for (int r = 0; r < 4; r++) {
                try {
                    Wake tile = (Wake) w.clone();
                    Pair<Wake, State> next = clone.applyMove(tile, r);
                    clone.hand.remove(tile);
                    clone.hand.add(next.a);
                    root.children.add(new StateTree(clone, root));
                } catch (GameWonException e) {
                    w.rot = r;
                    init.board[xy.a][xy.b] = w;
                    return constructMoveStr(xy, init);
                } catch (InvalidMoveException ignored) {}
            }
        }

        while (System.currentTimeMillis() < end) {
            StateTree node = selectNode(root);
            assert node != null;

            backpropagate(node, simulate(node.data));
        }

        Node ret = null;
        for (RoseTree<Node> c : root.children) {
            ret = ret == null || c.data.a > ret.a ? c.data : ret;
        }

        if (ret == null) {
            // We have lost the game
            return String.format("%02d%d%d%d", init.hand.get(0).id, xy.a, xy.b, 0);
        }
        else {
            return constructMoveStr(xy, ret.c);
        }
    }

    /**
     * Select node to roll out.
     *
     * @param root Root of tree to select from
     * @return Next node to roll out
     */
    private static StateTree selectNode(StateTree root) {
        // Select the node based on the best UCT
        return null;
    }

    /**
     * Simulates a random path through the search tree.
     *
     * @param n Node to apply a rollout to
     * @return 1 if the result was a win, and 0 if it was a loss or draw
     */
    private static int simulate(Node n) throws CloneNotSupportedException {
        State state = (State) n.c.clone();
        Random rand = new Random();

        while (true) {
            int dice = rand.nextInt(6) + rand.nextInt(6) + 2;
            if (dice > 5 && dice < 9) {
                try {
                    state.moveDaikaiju();
                } catch (GameLostException e) {
                    return 0;
                } catch (GameWonException e) {
                    return 1;
                }
            }

            State clone = (State) state.clone();
            Wake w = state.hand.remove(rand.nextInt(3));
            /*
            try {
                // Make a move
            } catch (GameWonException e) {
                return 1;
            } catch (GameLostException e) {
                return 0;
            } catch (InvalidMoveException e) {
                // Select another move
            }
            */

            // Simulate all other players by randomly generating their placements from the draw pile
        }
    }

    /**
     * Propagate the result of a simulation.
     *
     * @param t Node to propagate from
     * @param result Result to propagate
     */
    private static void backpropagate(StateTree t, int result) {
        if (t.parent != null) {
            t.data.a++;
            t.data.a += result;
            backpropagate((StateTree) t.parent, result);
        }
    }

    /**
     * Construct an action string.
     *
     * @param xy Coordinates of the tile placed
     * @param next State to look for placed tile
     * @return String representing a wake tile placement
     */
    private static String constructMoveStr(Pair<Integer, Integer> xy, State next) {
        Wake tile = (Wake) next.board[xy.a][xy.b];
        return String.format("%02d%d%d%d", tile.id, xy.a, xy.b, tile.rot);
    }
}
