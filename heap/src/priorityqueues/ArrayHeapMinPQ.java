package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T extends Comparable<T>> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;

    private int size;
    private HashMap<T, Integer> itemMap;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
        size = 0;
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> node1 = items.get(a);
        PriorityNode<T> node2 = items.get(b);
        itemMap.put(node1.getItem(), b);
        itemMap.put(node2.getItem(), a);
        items.set(a, node2);
        items.set(b, node1);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        } else {
            items.add(new PriorityNode<>(item, priority)); // add to the rightmost pos
            itemMap.put(item, size);
            swim(size);
            size++;
        }
    }

    @Override
    public boolean contains(T item) {
        return itemMap.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() <= 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size() <= START_INDEX) {
            throw new NoSuchElementException();
        }
        T removed = peekMin();
        swap(0, size - 1);
        itemMap.remove(removed);
        size--;
        sink(0);
        return removed;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (contains(item)) {
            int index = itemMap.get(item);
            if (priority < items.get(index).getPriority()) {
                items.get(index).setPriority(priority);
                swim(index);
            } else {
                items.get(index).setPriority(priority);
                sink(index);
            }
        } else {
            throw new NoSuchElementException();
        }

    }

    @Override
    public int size() {
        return size;
    }

    private int parentIndex(int childIndex) {
        return (childIndex + 1) / 2 - 1;
    }

    private int leftIndex(int parentIndex) {
        return (parentIndex + 1) * 2 - 1;
    }

    private int rightIndex(int parentIndex) {
        return (parentIndex + 1) * 2;
    }

    private void swim(int index) {
        if (index > 0) {
            int parent = parentIndex(index);
            if (items.get(index).getPriority() < items.get(parent).getPriority()) {
                swap(index, parent);
                swim(parent);
            }
        }
    }

    private void sink(int index) {
       int sinkIndex = findMin(index);
       if (sinkIndex > index) {
           swap(index, sinkIndex);
           sink(sinkIndex);
        }
    // while (items.get(index).getPriority() > items.get(sinkIndex).getPriority()) {
    //     swap(index, sinkIndex);```
    //     index = sinkIndex;
    //     sinkIndex = findMin(index);
    // }
    }

    private int findMin(int parentIndex) {
        int left = leftIndex(parentIndex);
        int right = rightIndex(parentIndex);
        if (left <= size() && items.get(left).getPriority() < items.get(parentIndex).getPriority()) {
            parentIndex = left;
        }
        if (right <= size() && items.get(right).getPriority() < items.get(parentIndex).getPriority()) {
            parentIndex = right;
        }
        return parentIndex;
    }
}
