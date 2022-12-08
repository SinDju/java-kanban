package manager;

import model.Task;

public class Node {
    public Task item;
    public Node next;
    public Node prev;

    public Node(Task item, Node next, Node prev) {
        this.item = item;
        this.next = next;
        this.prev = prev;
    }
}