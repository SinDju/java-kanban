import exception.ManagerSaveException;
import fileManager.FileBackedTasksManager;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private String path = "src/resource/historyTasksManager.csv";
    private File file = new File(path);

    @Override
    public FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(path);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(Path.of(path));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void correctlyLoadfromfileEndSave() {
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.getHistory();
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(file);

        assertTrue(fileManager.getTask(task.getId()).equals(task));
        assertTrue(fileManager.getEpic(epic.getId()).equals(epic));
        assertEquals(0, fileManager.getEpic(epic.getId()).getSubtaskIds().size());
    }

    @Test
    public void shouldSaveOfEmptyThrowException() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    FileBackedTasksManager.loadFromFile(file);
                });

        assertEquals("Не удалось считать данные из файла.", exception.getMessage());
    }

    @Test
    public void shouldSaveEmptyHistoryThrowException() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    FileBackedTasksManager.loadFromFile(file).getHistory();
                });

        assertEquals("Не удалось считать данные из файла.", exception.getMessage());
    }
}
