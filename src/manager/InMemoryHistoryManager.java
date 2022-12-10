package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
   /* CustomLinkedList customLinkedList = new CustomLinkedList(); - Привет, Патимат.
    Правильно ли я поняла, что класс для списка customLinkedList, т.е. и объект customLinkedList
    создавать не нужно? Или все таки нужен внутренний класс CustomLinkedList? Методы linkLast, getTasks и removeNode
    у меня получилось реализовать в классе InMemoryHistoryManager. Но само описание ТЗ немного сбивает с толку*/


    @Override
    public void add(Task task) {
        Integer taskId = task.getId();

        if (nodeMap.containsKey(taskId)) { // проверяем есть ли в мапе айди этого ключа и удаляем его если что
            Node node = nodeMap.get(taskId);
            nodeMap.remove(taskId);
            removeNode(node);
        }
        linkLast(task);
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
    public List<Task> getHistory() {
// Реализация метода getHistory должна перекладывать задачи из связного списка в ArrayList для формирования ответа.
        return getTasks();
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


        private Node head;
        private Node tail;
        private int size = 0;
        public void linkLast(Task task) {
            if(nodeMap.containsKey(task.getId())){
                removeNode(nodeMap.get(task.getId()));
                nodeMap.remove(task.getId());
            }
            final Node oldTail = tail;
            final Node newNode = new Node(task, null, oldTail);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;

            nodeMap.put(task.getId(), newNode);
        }

        public void removeNode(Node node) {

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

        public ArrayList<Task> getTasks() {
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
