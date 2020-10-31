package comp1140.ass2.util;

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
