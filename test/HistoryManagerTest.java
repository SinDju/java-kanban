import fileManager.HistoryManager;
import fileManager.InMemoryTaskManager;
import fileManager.Managers;
import fileManager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryManagerTest {
    HistoryManager historyManager;
    TaskManager taskManager;

    @BeforeEach
    public void createHistoryManager() {
        historyManager = Managers.getDefaultHistory();
        taskManager = new InMemoryTaskManager();
    }

    public Task createTask() {
        return new Task("Test addNewTask", "Test addNewTask description", NEW);
    }

    public Subtask createSubtask() {
        return new Subtask("Test addNewSubtask",
                "Test addNewSubtask description", NEW);
    }

    public Epic createEpic() {
        return new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
    }

    @Test
    void add() {
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
        Task task = createTask();

        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");

        historyManager.add(task);
        assertEquals(1, history.size(), "История дублируется");
    }

    @Test
    void remove() {
        Task task = createTask();
        final int taskId = taskManager.addNewTask(task);
        Epic epic = createEpic();
        final int epicId = taskManager.addNewEpic(epic);

        historyManager.add(task);
        historyManager.add(epic);

        historyManager.remove(taskId);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Задачи не удаляются из истории");
        historyManager.remove(epicId);
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая после удаления.");
    }

}