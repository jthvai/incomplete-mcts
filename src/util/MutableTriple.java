package comp1140.ass2.util;

/**
 * Mutable polymorphic triple.
 */
public class MutableTriple<R, S, T> {
    public R a;
    public S b;
    public T c;

    public MutableTriple(R a, S b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
