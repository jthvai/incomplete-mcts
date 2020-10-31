// SPDX-License-Identifier: CC0-1.0
package mcts.util;

/**
 * Immutable polymorphic pair.
 */
public class Pair<S, T> {
    public final S a;
    public final T b;

    public Pair(S a, T b) {
        this.a = a;
        this.b = b;
    }
}
