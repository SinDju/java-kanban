package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        Integer taskId = task.getId();

        if (nodeMap.containsKey(taskId)) { // проверяем есть ли в мапе айди этого ключа и удаляем его если что
            Node node = nodeMap.remove(taskId);
            removeNode(node);
        }
        linkLast(task);
        nodeMap.put(taskId, tail);
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Node node = nodeMap.get(id);
            nodeMap.remove(id);
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() { // перекладываем задачи из связного списка в ArrayList для формирования ответа
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node newNode = new Node(task, null, null);

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
    }

    private void removeNode(Node node) {
        final Node prev = node.prev;
        final Node next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.item = null;
    }

    private ArrayList<Task> getTasks() {
//getTasks собирает все задачи из  списка CustomLinkedList в обычный ArrayList.
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;

        while (node != null) {
            tasks.add(node.item);
            node = node.next;
        }
        return tasks;
    }

    public static class Node {
        public Task item;
        public Node next;
        public Node prev;

        public Node(Task item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
}
