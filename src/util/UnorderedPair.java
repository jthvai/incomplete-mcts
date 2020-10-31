// SPDX-License-Identifier: CC0-1.0
package mcts.util;

import java.util.HashSet;

public class UnorderedPair<T> extends HashSet<T>  {
    public UnorderedPair(T da, T db) {
        super(2);
        this.add(da);
        this.add(db);
    }

    public T getNot(T v) {
        for (T o : this) {
            if (!o.equals(v)) {
                return o;
            }
        }
        return null;
    }
}
