package comp1140.ass2.util;

import java.util.LinkedList;

/**
 * Polymorphic rose tree.
 */
public class RoseTree<T> {
    public T data;
    public RoseTree<T> parent;
    public LinkedList<RoseTree<T>> children;

    public RoseTree(T data, RoseTree<T> parent, LinkedList<RoseTree<T>> children) {
        this.data = data;
        this.parent = parent;
        this.children = children;
    }
}
