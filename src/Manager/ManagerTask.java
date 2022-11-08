package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ManagerTask {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int generatoreId = 0;

    public int addNewTask(Task task) { // Создание нового объекта Task
        final int id = ++generatoreId;

        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewSubtask(Subtask subtask) { // Создание нового объекта Subtask
        final int id = ++generatoreId;

        subtask.setId(id);
        int epicId = subtask.getEpicId();
        epics.get(epicId).addSubtaskIds(id);
        subtasks.put(id, subtask);
        return id;
    }

    public int addNewEpic(Epic epic) { // Создание нового объекта Epic
        final int id = ++generatoreId;

        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public ArrayList<Task> getTasks() { // кладем значение из мапы тасков в лист и возвращаем его
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    public ArrayList<Subtask> getSubtasks() { // кладем значение из мапы субтасков в лист и возвращаем его
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    public ArrayList<Epic> getEpics() { // кладем значение из мапы эпиков в лист и возвращаем его
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    public void updateTask(Task task) { // Обновление объекта task
        if(tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) { // Обновление объекта subtask
        if(subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Integer epicId = subtask.getEpicId();
            updateEpicStatus(epics.get(epicId));
        }
    }

    public void updateEpic(Epic epic) { // Обновление объекта epic
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public Task getTask(int id) { // Получение по идентификатору объекта Task
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) { // Получение по идентификатору объекта Subtask
        return subtasks.get(id);
    }

    public Epic getEpic(int id) { // Получение по идентификатору объекта Epic
        return epics.get(id);
    }

    public void deleteTasks() { // Удаление всех задач списка Task
        tasks.clear();
    }

    public void deleteSubtasks() { // Удаление всех задач списка Subtasks
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    public void deleteEpics() { // Удаление всех задач списка Epics
        subtasks.clear();
        epics.clear();
    }

    public void deleteTask(int id) { // Удаление по идентификатору объекта Task
        tasks.remove(id);
    }

    public void deleteSubtask(int id) { // Удаление по идентификатору объекта Subtask
        int idEpic = subtasks.get(id).getEpicId();
        epics.get(idEpic).removeSubtaskIds(id);
        subtasks.remove(id);
        updateEpicStatus(epics.get(idEpic));
    }

    public void deleteEpic(int id) { // Удаление по идентификатору объекта Epic
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtaskIds()) {
            subtasks.remove(idSubtask);
        }
        epic.cleanSubtaskIds();
        epics.remove(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) { // Получение списка всех подзадач определённого эпика
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer idSubtask : epic.getSubtaskIds()) {
            subtaskArrayList.add(subtasks.get(idSubtask));
        }
        return subtaskArrayList;
    }

    public void updateEpicStatus(Epic epic) { // Обновление статуса объекта Epic

        boolean isStatusNew = true;
        boolean isStatusDone = true;

        if (epic.getSubtaskIds() != null) {

            for (Integer idSubtask : epic.getSubtaskIds()) {
                Subtask subtask = getSubtask(idSubtask);
                if (!subtask.getStatus().equals(Status.NEW)) {
                    isStatusNew = false;
                }
                if (!subtask.getStatus().equals(Status.DONE)) {
                    isStatusDone = false;
                }
            }
            if (isStatusNew) {
                epic.setStatus(Status.NEW);
            } else if (isStatusDone) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            System.out.println(epic.getStatus());
        } else {
            System.out.println("У обекта эпика нет подзадач");
        }
    }
}
