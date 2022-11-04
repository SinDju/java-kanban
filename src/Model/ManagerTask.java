package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

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

    public int addNewSubtask(Subtask subtask, Integer epicId) { // Создание нового объекта Subtask
        final int id = ++generatoreId;
        subtask.setId(id);
        subtask.setEpicId(epicId);
        epics.get(epicId).setSubtaskIds(id);
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

    public ArrayList<Subtask> getSubtask() { // кладем значение из мапы субтасков в лист и возвращаем его
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    public ArrayList<Epic> getEpic() { // кладем значение из мапы эпиков в лист и возвращаем его
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    public void updateTask(Task task, String name, String description, Status status) { // Обновление объекта
        Integer id = task.getId();

        Task taskNew = new Task(name, description, status);
        taskNew.setId(id);
        tasks.put(id, taskNew);
    }

    public void updateSubtask(Subtask subtask, String name, String description, Status status) { // Обновление объекта
        Integer id = subtask.getId();
        Integer epicId = subtask.getEpicId();

        Subtask subtaskNew = new Subtask(name, description, status);
        subtaskNew.setId(id);
        subtaskNew.setEpicId(epicId);
        subtasks.put(id, subtaskNew);

    }

    public void updateEpic(Epic epic, String name, String description) { // Обновление объекта
        Integer idEpic = epic.getId();
        ArrayList<Integer> idsubtasks = epic.getSubtaskIds();
        Status status = epic.getStatus();

        Epic epicNew = new Epic(name, description, status);
        epicNew.setId(idEpic);
        epicNew.setIdSubtaskIds(idsubtasks);
        epics.put(idEpic, epicNew);
    }

    public void searchForIdTask(int id) { // Получение по идентификатору объекта Task
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                System.out.println("Задача по ID: " + id + " найдена: " + tasks.get(id));
            } else {
                System.out.println("Задачи с ID: " + id + " нет в списке");
            }
        } else {
            System.out.println("Невозможно найти задачу с ID: " + id + " так как список пуст.");
        }
    }

    public void searchForIdSubtasks(int id) { // Получение по идентификатору объекта Subtasks
        if (!subtasks.isEmpty()) {
            if (subtasks.containsKey(id)) {
                System.out.println("Задача по ID: " + id + " найдена: " + subtasks.get(id));
            } else {
                System.out.println("Задачи с ID: " + id + " нет в списке");
            }
        } else {
            System.out.println("Невозможно найти задачу с ID: " + id + " так как список пуст.");
        }
    }

    public void searchForIdEpic(int id) { // Получение по идентификатору объекта Epic
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                System.out.println("Задача по ID: " + id + " найдена: " + epics.get(id));
            } else {
                System.out.println("Задачи с ID: " + id + " нет в списке");
            }
        } else {
            System.out.println("Невозможно найти задачу с ID: " + id + " так как список пуст.");
        }
    }

    public void deleteTask() { // Удаление всех задач списка Task
        if (!tasks.isEmpty()) {
            tasks.clear();
            System.out.println(" Все задачи из списка Task удалены.");
        } else {
            System.out.println("Невозможно удалить задачи, так как список Task пуст.");
        }
    }

    public void deleteSubtasks() { // Удаление всех задач списка Subtasks
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            System.out.println(" Все задачи из списка Task удалены.");
        } else {
            System.out.println("Невозможно удалить задачи, так как список Task пуст.");
        }
    }

    public void deleteEpics() { // Удаление всех задач списка Epics
        if (epics.isEmpty()) {
            System.out.println("Невозможно удалить задачи, так как список пуст.");
        } else {
            for (int idEpic : epics.keySet()) {
                epics.get(idEpic).cleanSubtaskIds();
            }
            epics.clear();
            System.out.println(" Все задачи из списка Epic удалены.");
        }
    }

    public void deleteOfIdTask(int id) { // Удаление по идентификатору объекта Task
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача с ID: " + id + " удалена из списка.");
        } else {
            System.out.println("Невозможно удалить задачу, так как задачи с таким ID не существует.");
        }
    }

    public void deleteOfIdSubtasks(int id) { // Удаление по идентификатору объекта Subtasks
        if (subtasks.containsKey(id)) {
            int idEpic = subtasks.get(id).getEpicId();
            epics.get(idEpic).removeSubtaskIds(id);
            subtasks.remove(id);
            System.out.println("Задача с ID: " + id + " удалена из списка.");
        } else {
            System.out.println("Невозможно удалить задачу, так как задачи с таким ID не существует.");
        }
    }

    public void deleteOfIdEpics(int id) { // Удаление по идентификатору объекта Epics
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer idSubtask : epic.getSubtaskIds()) {
                subtasks.remove(idSubtask);
            }
            epic.cleanSubtaskIds();
            epics.remove(id);
            System.out.println("Задача с ID: " + id + " удалена из списка.");
        } else {
            System.out.println("Невозможно удалить задачу, так как задачи с таким ID не существует.");
        }
    }

    public void printSubtasksOfEpic(Epic epic) { // Получение списка всех подзадач определённого эпика
        if (epic != null) {
            for (Integer idSubtask : epic.getSubtaskIds()) {
                System.out.println(subtasks.get(idSubtask));
            }
        } else {
            System.out.println("Список " + epic.name + " пуст.");
        }
    }

    public Subtask getSubtask(Integer id) { // Извлекаем статусы Subtasks из объекта epic
        Subtask subtask = subtasks.get(id);
        return subtask;
    }

    public void updateStatusOfEpic(Epic epic) { // Обновление статуса объекта Epic

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


    @Override
    public String toString() {
        String result = "ManagerTask{";
        if (!tasks.isEmpty()) {
            result = result + "tasks=" + tasks;
        } else {
            result = result + "tasks=null";
        }
        if (!subtasks.isEmpty()) {
            result = result + ", subtasks=" + subtasks;
        } else {
            result = result + ", subtasks= null";
        }
        if (!epics.isEmpty()) {
            result = result + ", epics=" + epics;
        } else {
            result = result + ", epics=null";
        }
        return result + "}";
    }


}
