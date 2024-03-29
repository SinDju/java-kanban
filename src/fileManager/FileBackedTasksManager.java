package fileManager;

import exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static fileManager.TasksType.EPIC;

public class FileBackedTasksManager extends InMemoryTaskManager {
    //private static Path path;
    private static String path;

    public FileBackedTasksManager(String path) {
        this.path = path;// новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле
    }

    public FileBackedTasksManager() {

    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = super.getHistory();
        save();
        return taskList;
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> subtaskArrayList = super.getEpicSubtasks(epicId);
        save();
        return subtaskArrayList;
    }

    protected void save() {
        try (FileWriter writer = new FileWriter(path, false)) {

            writer.write("id,type,name,status,description,epic,duration,startTime,startTimeMinutes," +
                    "endTime,endTimeMinutes\n");
            for (Task task : tasks.values()) {
                String lineTask = toString(task);
                writer.write(lineTask);
            }
            for (Task epic : epics.values()) {
                writer.write(toString(epic));
            }
            for (Task subtask : subtasks.values()) {
                writer.write(toString(subtask));
            }
            writer.write("\n");
            writer.write(historyToString(historyManager)); // так как у нас протектед мы при вызове метода
            // автоматически добавляем историю в нашу мапу и потом извлекаем из нее Id тасков и сохраняем
        } catch (IOException e) {
            throw new ManagerSaveException("Что-то пошло не так");
        }
    }

    private String toString(Task task) { // сохранение задачи в строку
        if (task.getStartTime() == null) {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription() + "," + task.getEpicId() + "\n";
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getEpicId() + "," + task.getDuration().toMinutes() + ","
                + task.getStartTime() + ","
                + task.getEndTime() + "\n";
    }

    public static String historyToString(HistoryManager manager) {
        String lineIds = "";
        List<Task> tasksHistory = manager.getHistory();
        for (int i = 0; i < tasksHistory.size(); i++) {
            if (i == tasksHistory.size() - 1) {
                lineIds = lineIds + tasksHistory.get(i).getId();
            } else {
                lineIds = lineIds + tasksHistory.get(i).getId() + ",";
            }
        }
        return lineIds;
    }

    private static List<Integer> historyFromString(String value) {// восстановление истории задач в HistoryManager manager
        List<Integer> tasksId = new ArrayList<>();
        if(value != null) {
            String[] historyIdTasks = value.split(",");

            for (String id : historyIdTasks) {
                int idTask = Integer.valueOf(id);
                tasksId.add(idTask);
            }
        }
        return tasksId;
    }

    private static Task fromString(String value) { //  создание задачи из строки
        String[] linesTask = value.split(",", 11);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");

        int id = Integer.valueOf(linesTask[0]);
        String name = linesTask[2];
        TasksType tasksType = TasksType.valueOf(linesTask[1]);
        Status status = Status.valueOf(linesTask[3]);
        String description = linesTask[4];
        Integer duration = linesTask.length > 6 ? Integer.valueOf(linesTask[6]) : null;
        LocalDateTime startTime = linesTask.length > 6 ? LocalDateTime.parse(linesTask[7] + ","
                + linesTask[8], formatter) : null;
        LocalDateTime endTime = linesTask.length > 10? LocalDateTime.parse(linesTask[9]
                + "," + linesTask[10], formatter) : null;


        switch (tasksType) {
            case TASK:
                if (duration == null) {
                    return new Task(id, name, description, status);
                }
                return new Task(id, name, description, status, duration,
                        startTime);
            case EPIC:
                if (duration == null) {
                    return new Epic(id, name, description, status);
                }
                return new Epic(id, name, description, status, duration,
                        startTime, endTime);
            case SUBTASK:
                String epicIdString = linesTask[5].trim();
                if (duration == null) {
                    return new Subtask(id, name, description, status, Integer.valueOf(epicIdString));
                }
                return new Subtask(id, name, description, status, Integer.valueOf(epicIdString),
                        duration, startTime);
            default:
                return null;
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) { // восстанавливает данные менеджера из файла
        // при запуске программы
        FileBackedTasksManager taskManager1 = new FileBackedTasksManager(file.toString());

        int maxId = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if(line.contains("id")) {
                    continue;
                }
                if (line.equals("")) {
                    break;
                }
                Task task = fromString(line);
                TasksType tasksType = task.getType();
                Subtask subtask;
                Epic epic;

                switch (tasksType) {
                    case SUBTASK:
                        subtask = (Subtask) task;
                        Integer epicId = subtask.getEpicId();
                        taskManager1.subtasks.put(subtask.getId(), subtask);
                        taskManager1.epics.get(epicId).addSubtaskIds(subtask.getId());
                        if (subtask.getId() > maxId) {
                            maxId = subtask.getId();
                        }
                        break;
                    case EPIC:
                        epic = (Epic) task;
                        taskManager1.epics.put(epic.getId(), epic);
                        if (epic.getId() > maxId) {
                            maxId = epic.getId();
                        }
                        break;
                    case TASK:
                        taskManager1.tasks.put(task.getId(), task);
                        if (task.getId() > maxId) {
                            maxId = task.getId();
                        }
                        break;
                }

            }
            taskManager1.generatoreId = maxId;
            String lineHistory = bufferedReader.readLine();
                List<Integer> idTasks = new ArrayList<>(historyFromString(lineHistory));
                for (Integer id : idTasks) {
                    if (taskManager1.epics.containsKey(id)) {
                        taskManager1.historyManager.add(taskManager1.epics.get(id));
                    } else if (taskManager1.subtasks.containsKey(id)) {
                        taskManager1.historyManager.add(taskManager1.subtasks.get(id));
                    } else {
                        taskManager1.historyManager.add(taskManager1.tasks.get(id));
                    }
                }
            return taskManager1;
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }

}
