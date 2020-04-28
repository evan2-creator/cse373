package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 3;

    /*
    Warning:
    DO NOT rename this `entries` field or change its type.
    We will be inspecting it in our Gradescope-only tests.

    An explanation of this field:
    - `entries` is the array where you're going to store all of your data (see the [] square bracket notation)
    - The other part of the type is the SimpleEntry<K, V> -- this is saying that `entries` will be an
    array that can store a SimpleEntry<K, V> object at each index.
       - SimpleEntry represents an object containing a key and a value.
        (To jump to its details, middle-click or control/command-click on SimpleEntry below)

    */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!
    private int size;

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    private int keyIndex(Object key) {
        for (int i = 0; i < size; i++) {
            //if (entries[i].getKey().equals(key)) {
            if (entries[i].getKey()==key || key != null && entries[i].getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public V get(Object key) {
        if (containsKey(key)) {
            return entries[keyIndex(key)].getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (containsKey(key)) {
            return entries[keyIndex(key)].setValue(value);
        }
        size++;
        if (size > entries.length) {
            SimpleEntry<K, V>[] oldEntries = entries;
            entries = createArrayOfEntries(entries.length * 2);
            System.arraycopy(oldEntries, 0, entries, 0, size - 1);
        }
        entries[size - 1] = new SimpleEntry<>(key, value);
        return null;
    }

    @Override
    public V remove(Object key) {
        V oldValue = null;
        if (containsKey(key)) {
            int index = keyIndex(key);
            oldValue = entries[index].getValue();
            entries[index] = entries[size - 1];
            size--;
        }
        return oldValue;
    }

    @Override
    public void clear() {
        entries = createArrayOfEntries(entries.length);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return keyIndex(key) > -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ArrayMapIterator<>(this.entries, this.size);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int n = 0;
        private final int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return n < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Map.Entry<K, V> result = entries[n];
            n++;
            return result;
        }
    }
}
