import fileManager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.Status.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    protected Task task = createTask();
    protected Epic epic = createEpic();
    protected Subtask subtask = createSubtask();

    public abstract T createTaskManager();

    @BeforeEach
    public void BeforeEach() {
        taskManager = createTaskManager();
    }

    public Task createTask() {
        return new Task("Test addNewTask", "Test addNewTask description", NEW);
    }

    public Subtask createSubtask() {
        return new Subtask("Test addNewSubtask", "Test addNewSubtask description", NEW);
    }

    public Epic createEpic() {
        return new Epic("Test addNewEpic", "Test addNewEpic description", NEW);
    }

    @Test
    void addNewTask() {

        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        taskManager.deleteTask(taskId);
        assertFalse(taskManager.getTasks().contains(task), "Задачи не удаляются.");
    }

    @Test
    void addNewEpic() {
        final int epicId = taskManager.addNewEpic(epic);
        final Task savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(NEW, epic.getStatus(), "Статусы при отсутствии подзадач не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");

        taskManager.deleteEpic(epicId);
        assertFalse(taskManager.getEpics().contains(epic), "Задачи не удаляются.");
    }

    @Test
    void addNewSubtask() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        final int subtaskEpicId = taskManager.getSubtask(subtaskId).getEpicId();

        assertNotNull(taskManager.getEpic(subtaskEpicId), "Задача Epic не найдена по ключу subtaskId.");
        assertEquals(epicId, subtaskEpicId, "EpicId не совпадают.");
        assertEquals(epic, taskManager.getEpic(subtaskEpicId), "Задачи Epic не совпадают.");


        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getHistory() {
        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста."); // Пустой список задач.
        final int taskId = taskManager.addNewTask(task);
        taskManager.getTask(taskId);
        assertFalse(taskManager.getHistory().isEmpty(), "История пуста."); // стандартное поведение
        assertEquals(1, taskManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void getTasks() {
        assertTrue(taskManager.getTasks().isEmpty(), "Задач есть в пустом списке."); // Пустой список задач.

        taskManager.addNewTask(task);

        assertTrue(taskManager.getTasks().contains(task), "Задач не совпадают."); // стандартное поведение
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач."); // неверный
        // идентификатор задачи
    }

    @Test
    void getSubtasks() {
        assertTrue(taskManager.getSubtasks().isEmpty(), "Задач есть в пустом списке."); // Пустой список задач.

        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);

        taskManager.addNewSubtask(subtask);
        assertEquals(epic, taskManager.getEpic(subtask.getEpicId()),
                "У подзадачи неправильный идентефикатор эпика"); // наличие эпика

        assertTrue(taskManager.getSubtasks().contains(subtask), "Задач не совпадают."); // стандартное поведение
        assertEquals(1, taskManager.getSubtasks().size(), "Неверное количество задач."); // неверный
        // идентификатор задачи
    }

    @Test
    void getEpics() {
        assertTrue(taskManager.getEpics().isEmpty(), "Задач есть в пустом списке."); // Пустой список задач.

        final int epicId = taskManager.addNewEpic(epic);
        assertEquals(0, taskManager.getEpic(epicId).getSubtaskIds().size(),
                "Неверное количество подзадач.");

        subtask.setEpicId(epicId);

        taskManager.addNewSubtask(subtask);

        assertTrue(taskManager.getEpics().contains(epic), "Задач не совпадают."); // стандартное поведение
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач."); // неверный
        // идентификатор задачи
    }

    @Test
    void updateTask() {
        taskManager.addNewTask(task);
        assertEquals(NEW, task.getStatus());

        task.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, task.getStatus());

        task.setStatus(DONE);
        assertEquals(DONE, task.getStatus());
    }

    @Test
    void updateSubtask() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);

        taskManager.addNewSubtask(subtask);
        assertEquals(NEW, subtask.getStatus());

        subtask.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, subtask.getStatus());

        subtask.setStatus(DONE);
        assertEquals(DONE, subtask.getStatus());
    }

    @Test
    void updateEpic() {
        final int epicId = taskManager.addNewEpic(epic);
        assertEquals(NEW, epic.getStatus(), "Статус при отсутствии подзадач не совпадает");

        subtask.setEpicId(epicId);
        taskManager.addNewSubtask(subtask);

        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask description2", NEW);
        subtask2.setEpicId(epicId);
        taskManager.addNewSubtask(subtask2);

        assertEquals(NEW, epic.getStatus(), "Статус при статусе подзадач NEW не совпадает");

        subtask.setName("Test doneSubtask1");
        subtask.setDescription("Test doneSubtask description1");
        subtask.setStatus(DONE);
        taskManager.updateSubtask(subtask);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы при NEW и DONE подзадач не совпадают.");

        subtask2.setName("Test doneSubtask2");
        subtask2.setDescription("Test doneSubtask description2");
        subtask2.setStatus(DONE);

        taskManager.updateSubtask(subtask2);
        assertEquals(DONE, epic.getStatus(), "Статусы при DONE и DONE подзадач не совпадают.");

        subtask.setStatus(IN_PROGRESS);
        subtask2.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы при IN_PROGRESS и IN_PROGRESS подзадач " +
                "не совпадают.");
    }

    @Test
    void getTask() {
        final int taskId = taskManager.addNewTask(task);
        assertEquals(1, taskId, "Id задач не совпадают");
    }

    @Test
    void getSubtask() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);

        final int subtaskId = taskManager.addNewSubtask(subtask);

        assertEquals(2, subtaskId, "Id задач не совпадают");
    }

    @Test
    void getEpic() {
        final int epicId = taskManager.addNewEpic(epic);
        assertEquals(1, epicId, "Id задач не совпадают");
    }

    @Test
    void deleteTasks() {
        taskManager.addNewTask(task);
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getTasks().size()
                , "Задачи не удаляются");
    }

    @Test
    void deleteSubtasks() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);
        taskManager.addNewSubtask(subtask);

        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasks().size()
                , "Подзадачи не удаляются");
        assertEquals(0, taskManager.getEpic(epicId).getSubtaskIds().size(),
                "Подзадачи Epica не удаляются");
    }

    @Test
    void deleteEpics() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);
        taskManager.addNewSubtask(subtask);

        taskManager.deleteEpics();
        assertEquals(0, taskManager.getEpics().size()
                , "Epic задачи не удаляются");
        assertEquals(0, taskManager.getSubtasks().size()
                , "Подзадачи Epic не удаляются");
    }

    @Test
    void deleteTask() {
        final int taskId = taskManager.addNewTask(task);
        taskManager.deleteTask(taskId);
        assertEquals(0, taskManager.getTasks().size()
                , "Задачи не удаляются");
    }

    @Test
    void deleteSubtask() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);

        final int subtaskId = taskManager.addNewSubtask(subtask);

        taskManager.deleteSubtask(subtaskId);
        assertEquals(0, taskManager.getSubtasks().size()
                , "Подзадачи не удаляются");
        assertEquals(0, taskManager.getEpic(epicId).getSubtaskIds().size(),
                "Подзадачи Epica не удаляются");
    }

    @Test
    void deleteEpic() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);
        taskManager.addNewSubtask(subtask);

        taskManager.deleteEpic(epicId);
        assertEquals(0, taskManager.getEpics().size()
                , "Epic задачи не удаляются");
        assertEquals(0, taskManager.getSubtasks().size()
                , "Подзадачи Epic не удаляются");
    }

    @Test
    void getEpicSubtasks() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);
        final int subtaskId = taskManager.addNewSubtask(subtask);
        Subtask subtaskEpic = taskManager.getSubtask(taskManager.getEpic(epicId).getSubtaskIds().get(0));

        assertEquals(taskManager.getSubtask(subtaskId), subtaskEpic, "Подзадачи не совпадают");
    }

    @Test
    void getPrioritizedTasks() {
        final int epicId = taskManager.addNewEpic(epic);
        subtask.setEpicId(epicId);
        subtask.setStartTime(LocalDateTime.of(2023,12,10,12,0));
        subtask.setDuration(Duration.ofMinutes(15));

        Task task = createTask();
        final int taskId = taskManager.addNewTask(task);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(25));

        assertEquals(LocalDateTime.of(2023,12,10,12,0), subtask.getStartTime());
        assertTrue(taskManager.getPrioritizedTasks().contains(taskManager.getTask(taskId)));
    }
}


