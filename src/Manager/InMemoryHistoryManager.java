package Manager;

import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> tasksHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        while (tasksHistory.size() > 10) {
            tasksHistory.remove(0);
        }
        return tasksHistory;
    }

}
