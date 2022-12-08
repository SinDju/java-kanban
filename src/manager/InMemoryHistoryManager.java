package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasksHistory = new ArrayList<>();

    CustomLinkedList customLinkedList = new CustomLinkedList();


    @Override
    public void add(Task task) {
        Integer taskId = task.getId();

        if (customLinkedList.customHashMap.containsKey(taskId)) {
            Node node = customLinkedList.customHashMap.get(taskId);
            customLinkedList.customHashMap.remove(taskId);
            customLinkedList.removeNode(node);// проверяем есть ли в мапе айди этого ключа и удаляем его если что
        }
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (tasksHistory.contains(id)) {
            tasksHistory.remove(id);
            Node node = customLinkedList.customHashMap.get(id);
            customLinkedList.customHashMap.remove(id);
            customLinkedList.removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
// Реализация метода getHistory должна перекладывать задачи из связного списка в ArrayList для формирования ответа.
        tasksHistory.addAll(customLinkedList.getHistory());

        return tasksHistory;
    }
}

class CustomLinkedList {
    /**
     * Указатель на первый элемент списка. Он же first
     */
    private Node head;

    /**
     * Указатель на последний элемент списка. Он же last
     */
    private Node tail;
    private int size = 0;

    Map<Integer, Node> customHashMap = new HashMap<>();

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(task, null, oldTail);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;

        customHashMap.put(task.getId(), newNode);
    }

    public void removeNode(Node node) {
        final Task task = node.item;
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
        size--;

    }

    public ArrayList<Task> getHistory() {
//getTasks собирает все задачи из  списка CustomLinkedList в обычный ArrayList.
        ArrayList<Task> taskArrayList = new ArrayList<>();
        Node task = head;
        for (int i = 0; i < size; i++) {
            taskArrayList.add(task.item);
            task = task.next;

            if (task == null) {
                return taskArrayList;
            }
        }
        return taskArrayList;
    }
}