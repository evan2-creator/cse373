package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */
    private final HashMap<T, Integer> map;

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>();
        this.map = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        pointers.add(-1);
        map.put(item, map.size());
    }

    @Override
    public int findSet(T item) {
        if (!map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        List<Integer> path = new ArrayList<>();
        int index = map.get(item);
        int to = pointers.get(index);
        while (to >= 0) {
            path.add(index);
            index = to;
            to = pointers.get(index);
        }
        for (int i : path) {
            pointers.set(i, index);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!map.containsKey(item1) || !map.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int id1 = findSet(item1);
        int id2 = findSet(item2);

        if (id1 == id2) {
            return false;
        }
        int size1 = Math.abs(pointers.get(id1));
        int size2 = Math.abs(pointers.get(id2));
        if (size1 >= size2) {
            pointers.set(id2, id1);
            pointers.set(id1, -(size1+size2));
        } else {
            pointers.set(id1, id2);
            pointers.set(id2, -(size1+size2));
        }
        return true;
    }
}
