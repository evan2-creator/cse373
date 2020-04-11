package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.
    T sentinel;

    public LinkedDeque() {
        size = 0;
        sentinel = (T) new Object();
        front = new Node<>(sentinel);
        back = new Node<>(sentinel);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> nextNode = new Node<>(item, front, front.next);
        front.next = nextNode;
        nextNode.next.prev = nextNode;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> prevNode = new Node<>(item, back.prev, back);
        back.prev = prevNode;
        prevNode.prev.next = prevNode;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> first = front.next;
        Node<T> nextNode = front.next.next;
        front.next = nextNode;
        nextNode.prev = front;
        return first.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> last = back.prev;
        Node<T> prevNode = back.prev.prev;
        back.prev = prevNode;
        prevNode.next = back;
        return last.value;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> curr;
        if (index > size / 2.0 - 1) {
            curr = back;
            for (int i = size - 1; i >= index; i--) {
                curr = curr.prev;
            }
        } else {
            curr = front;
            for (int i = 0; i <= index; i++) {
                curr = curr.next;
            }
        }
        return curr.value;
    }

    public int size() {
        return size;
    }
}
