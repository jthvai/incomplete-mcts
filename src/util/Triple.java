// SPDX-License-Identifier: CC0-1.0
package mcts.util;

/**
 * Immutable polymorphic triple.
 */
public class Triple<R, S, T> {
    public final R a;
    public final S b;
    public final T c;

    public Triple(R a, S b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
