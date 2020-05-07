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
    private HashMap<T, Integer> itemsMap;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        itemsMap = new HashMap<>();
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
        items.set(a, node2);
        items.set(b, node1);
        itemsMap.put(node1.getItem(), b);
        itemsMap.put(node2.getItem(), a);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item) || item == null) {
            throw new IllegalArgumentException();
        } else {
            items.add(new PriorityNode<>(item, priority));
            itemsMap.put(item, size);
            swimUp(size);
            size++;
        }
    }

    @Override
    public boolean contains(T item) {
        return itemsMap.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() > START_INDEX) {
            return items.get(START_INDEX).getItem();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public T removeMin() {
        if (size() > START_INDEX) {
            T removed = peekMin();
            swap(START_INDEX, size - 1);
            itemsMap.remove(removed);
            size--;
            swimDown(START_INDEX);
            return removed;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        if (contains(item)) {
            int index = itemsMap.get(item);
            if (priority < items.get(index).getPriority()) {
                items.get(index).setPriority(priority);
                swimUp(index);
            } else {
                items.get(index).setPriority(priority);
                swimDown(index);
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
        return (childIndex - 1) / 2;
    }

    private int leftIndex(int parentIndex) {
        return parentIndex * 2 + 1;
    }

    private int rightIndex(int parentIndex) {
        return parentIndex * 2 + 2;
    }

    private void swimUp(int index) {
        if (index > START_INDEX) {
            int parent = parentIndex(index);
            if (items.get(index).getPriority() < items.get(parent).getPriority()) {
                swap(index, parent);
                swimUp(parent);
            }
        }
    }

    private void swimDown(int index) {
        int sinkIndex = findSmaller(index);
        // while (items.get(index).getPriority() > items.get(sinkIndex).getPriority()) {
        //     swap(index, sinkIndex);
        //     index = sinkIndex;
        //     sinkIndex = findSmaller(index);
        // }
        if (sinkIndex > index) {
            swap(index, sinkIndex);
            swimDown(sinkIndex);
        }
    }

    private int findSmaller(int parentIndex) {
        int left = leftIndex(parentIndex);
        int right = rightIndex(parentIndex);
        if (left < size() && items.get(left).getPriority() < items.get(parentIndex).getPriority()) {
            parentIndex = left;
        }
        if (right < size() && items.get(right).getPriority() < items.get(parentIndex).getPriority()) {
            parentIndex = right;
        }
        return parentIndex;
    }
}
