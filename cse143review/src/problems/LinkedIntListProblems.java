package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        list.front.next.next.next = list.front;
        list.front = list.front.next.next;
        list.front.next.next.next = list.front.next;
        list.front.next = list.front.next.next;
        list.front.next.next.next = null;
    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        if (list.front != null && list.front.next != null) {
            ListNode temp = list.front;
            list.front = list.front.next;
            ListNode curr = list.front;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = temp;
            temp.next = null;
        }
    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        // Hint: you'll need to use the 'new' keyword to construct new objects.
        LinkedIntList result = new LinkedIntList();
        result.front = concatenate(a.front, b.front);
        return result;
    }

    private static ListNode concatenate(ListNode a, ListNode b) {
        if (a != null) {
            ListNode curr = new ListNode(a.data);
            if (a.next == null) {
                curr.next = b;
            } else {
                curr.next = concatenate(a.next, b);
            }
            return curr;
        }
        return b;
    }
}
