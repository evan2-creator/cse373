package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.6;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 3;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 4;

    /*
    Warning:
    DO NOT rename this `chains` field or change its type.
    We will be inspecting it in our Gradescope-only tests.

    An explanation of this field:
    - `chains` is the main array where you're going to store all of your data (see the [] square bracket notation)
    - The other part of the type is the AbstractIterableMap<K, V> -- this is saying that `chains` will be an
    array that can store an AbstractIterableMap<K, V> object at each index.
       - AbstractIterableMap represents an abstract/generalized Map. The ArrayMap you wrote in the earlier part
       of this project qualifies as one, as it extends the AbstractIterableMap class.  This means you can
       and should be creating ArrayMap objects to go inside your `chains` array as necessary. See the instructions on
       the website for diagrams and more details.
        (To jump to its details, middle-click or control/command-click on AbstractIterableMap below)
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int chainSize;
    private double loadFactor;

    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        chains = createArrayOfChains(initialChainCount);
        for (int i = 0; i < chains.length; i++) {
            chains[i] = createChain(chainInitialCapacity);
        }
        loadFactor = resizingLoadFactorThreshold;
        chainSize = 0;

    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    private int setHashCode(Object key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % chains.length;
        } else {
            return 0;
        }
    }

    @Override
    public V get(Object key) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null || !containsKey(key)) {
            return null;
        } else {
            return chains[hashCode].get(key);
        }
    }

    @Override
    public V put(K key, V value) {
        int hashCode = setHashCode(key);
        if (containsKey(key)) {
            return chains[hashCode].put(key, value);
        }

        if (chains[hashCode] == null) {
            chains[hashCode] = new ArrayMap<>();
        }
        int oldSize = chains[hashCode].size();
        chains[hashCode].put(key, value);
        chainSize += (chains[hashCode].size() - oldSize);

        if (1.0 * chainSize / chains.length >= loadFactor) {
            chains = resize();
        }
        return null;
    }

    private AbstractIterableMap<K, V>[] resize() {
        AbstractIterableMap<K, V>[] newChains = createArrayOfChains(chains.length * 2);
        for (Map.Entry<K, V> pair : this) {
            int newHashCode = Math.abs(pair.getKey().hashCode()) % newChains.length;
            if (newChains[newHashCode % newChains.length] == null) {
                newChains[newHashCode % newChains.length] = new ArrayMap<>();
            }
            newChains[newHashCode % newChains.length].put(pair.getKey(), pair.getValue());
        }
        return newChains;
    }

    @Override
    public V remove(Object key) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null || !chains[hashCode].containsKey(key)) {
            return null;
        } else {
            chainSize--;
            return chains[hashCode].remove(key);
        }
    }

    @Override
    public void clear() {
        chains = createArrayOfChains(chains.length);
        chainSize = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hashCode = setHashCode(key);
        if (chains[hashCode] == null) {
            return false;
        } else {
            return chains[hashCode].containsKey(key);
        }
    }

    @Override
    public int size() {
        return chainSize;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private int n;
        private Iterator<Map.Entry<K, V>> iter;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.n = 0;
            if (chains[n] != null) {
                iter = chains[n].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = n; i < chains.length; i++) {
                if (iter != null) {
                    if (iter.hasNext()) {
                        return true;
                    }
                }
                if (n == chains.length - 1) {
                    return false;
                }
                n++;
                if (chains[n] != null) {
                    iter = chains[n].iterator();
                } else {
                    iter = null;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                return iter.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
