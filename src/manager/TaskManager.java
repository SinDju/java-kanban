package manager;

import model.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    public List<Task> getHistory();

    public int addNewTask(Task task);

    public int addNewSubtask(Subtask subtask);

    public int addNewEpic(Epic epic);

    public ArrayList<Task> getTasks();

    public ArrayList<Subtask> getSubtasks();

    public ArrayList<Epic> getEpics();

    public void updateTask(Task task);

    public void updateSubtask(Subtask subtask);

    public void updateEpic(Epic epic);

    public Task getTask(int id);

    public Subtask getSubtask(int id);

    public Epic getEpic(int id);

    public void deleteTasks();

    public void deleteSubtasks();

    public void deleteEpics();

    public void deleteTask(int id);

    public void deleteSubtask(int id);

    public void deleteEpic(int id);

    public ArrayList<Subtask> getEpicSubtasks(int epicId);

    public void updateEpicStatus(Epic epic);

}
