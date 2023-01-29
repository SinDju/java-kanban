import fileManager.HistoryManager;
import fileManager.Managers;
import fileManager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HTTPTaskManager;
import server.KVServer;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest {
    private KVServer server;
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault("http://localhost:8078");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = new Task("name1", "description1", Status.NEW);
        Task task2 = new Task("name2", "description2", Status.NEW);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getTasks(), list);
    }

    @Test
    public void shouldLoadEpics() {
        Epic epic1 = new Epic("name1", "description1", Status.NEW);
        Epic epic2 = new Epic("name2", "description2", Status.NEW );
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        assertEquals(epic1, manager.getEpic(epic1.getId()));
        assertEquals(epic2, manager.getEpic(epic2.getId()));
        List<Task> list = manager.getHistory();
        assertEquals(manager.getEpics(), list);
    }

    @Test
    public void shouldLoadSubtasks() {
        Epic epic1 = new Epic("description1", "name1", Status.NEW);
        manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("name1", "description1", Status.NEW);
        subtask1.setEpicId(epic1.getId());
        Subtask subtask2 = new Subtask("name2", "description2", Status.NEW);
        subtask2.setEpicId(epic1.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getSubtasks(), list);
    }

}