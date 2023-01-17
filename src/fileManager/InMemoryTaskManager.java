package fileManager;

import model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected int generatoreId = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final TreeSet<Task> treeSet = new TreeSet<>();

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        treeSet.addAll(tasks.values());
        treeSet.addAll(subtasks.values());

        return treeSet;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int addNewTask(Task task) { // Создание нового объекта Task
        final int id = ++generatoreId;

        task.setId(id);
        if (task.getStartTime() != null) {
            if (getPrioritizedTasks().contains(task)) {
                System.out.println("Задача с данным временем уже задана");
                return id;
            }
        }
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) { // Создание нового объекта Subtask
        final int id = ++generatoreId;


        if (subtask.getStartTime() != null) {
            if (getPrioritizedTasks().contains(subtask)) {
                System.out.println("Задача с данным временем уже задана");
                return id;
            }
        }

        subtask.setId(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtaskIds(id);
        subtasks.put(id, subtask);
        if (subtask.getStartTime() != null) {
            if (epic.getSubtaskIds().size() == 1) {
                epic.setDuration(subtask.getDuration());
                epic.setStartTime(subtask.getStartTime());
                epic.setEndTime(subtask.getEndTime());
            } else {
                for (Integer idSubtask : epic.getSubtaskIds()) {
                    if (epic.getStartTime().isAfter(subtasks.get(idSubtask).getStartTime())) {
                        epic.setDuration(subtasks.get(idSubtask).getDuration());
                        epic.setStartTime(subtasks.get(idSubtask).getStartTime());
                    }
                    if (epic.getEndTime().isBefore(subtasks.get(idSubtask).getEndTime())) {
                        epic.setEndTime(subtasks.get(idSubtask).getEndTime());
                    }
                }
            }
        }
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) { // Создание нового объекта Epic
        final int id = ++generatoreId;

        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public ArrayList<Task> getTasks() { // кладем значение из мапы тасков в лист и возвращаем его
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() { // кладем значение из мапы субтасков в лист и возвращаем его
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    @Override
    public ArrayList<Epic> getEpics() { // кладем значение из мапы эпиков в лист и возвращаем его
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    public Boolean getBooleanPrioritizedTasks(Task task) {
        Boolean isTask = true;
        for (Task taskPrioritized : treeSet) {
            if (task.getStringStartTime().equals(taskPrioritized.getStringStartTime())) {
                isTask = false;
            }
        }
        return isTask;
    }

    @Override
    public void updateTask(Task task) { // Обновление объекта task
        if (tasks.containsKey(task.getId())) {
            if (task.getStartTime() == null) {
                tasks.put(task.getId(), task);
            } else if (getBooleanPrioritizedTasks(task)) {
                tasks.put(task.getId(), task);
            } else {
                System.out.println("Tакое время начала задачи уже занято");
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) { // Обновление объекта subtask
        if (subtasks.containsKey(subtask.getId())) {
            if (subtask.getStartTime() == null) {
                subtasks.put(subtask.getId(), subtask);
                Integer epicId = subtask.getEpicId();
                updateEpicStatus(epics.get(epicId));
            } else if (subtasks.get(subtask.getId()).getStartTime().equals(subtask.getStartTime())) {
                // если дата остается прежней просто меняем
                subtasks.put(subtask.getId(), subtask);
                Integer epicId = subtask.getEpicId();
                updateEpicStatus(epics.get(epicId));
            } else if (!getBooleanPrioritizedTasks(subtask)) {
                System.out.println("Tакое время начала задачи уже занято");
            } else {
                subtasks.put(subtask.getId(), subtask);
                Integer epicId = subtask.getEpicId();
                updateEpicStatus(epics.get(epicId));
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) { // Обновление объекта epic
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public Task getTask(int id) { // Получение по идентификатору объекта Task
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) { // Получение по идентификатору объекта Subtask
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) { // Получение по идентификатору объекта Epic
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    public List<Task> getDefaultHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteTasks() { // Удаление всех задач списка Task
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() { // Удаление всех задач списка Subtasks
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpics() { // Удаление всех задач списка Epics
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();

        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public void deleteTask(int id) { // Удаление по идентификатору объекта Task
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteSubtask(int id) { // Удаление по идентификатору объекта Subtask
        int idEpic = subtasks.get(id).getEpicId();
        epics.get(idEpic).removeSubtaskIds(id);
        subtasks.remove(id);
        updateEpicStatus(epics.get(idEpic));
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) { // Удаление по идентификатору объекта Epic
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtaskIds()) {
            subtasks.remove(idSubtask);
            historyManager.remove(idSubtask);
        }
        epic.cleanSubtaskIds();
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) { // Получение списка всех подзадач определённого эпика
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer idSubtask : epic.getSubtaskIds()) {
            subtaskArrayList.add(subtasks.get(idSubtask));
        }
        return subtaskArrayList;
    }

    private void updateEpicStatus(Epic epic) { // Обновление статуса объекта Epic

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
            System.out.println("У объекта эпика нет подзадач");
        }
    }
}
