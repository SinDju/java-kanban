import fileManager.FileBackedTasksManager;
import fileManager.Managers;
import fileManager.TaskManager;
import model.*;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault(Path.of("src/resource/historyTasksManager.csv"));
        Task task1 = new Task("таск1", "задача таска1", Status.NEW, 15, LocalDateTime.now());
        final Integer taskId1 = taskManager.addNewTask(task1);
        System.out.println(taskManager.getTasks());

        Task updatetask1 = taskManager.getTask(taskId1);
        updatetask1.setName("обнавленный таск1");
        updatetask1.setDescription("задача таска1");
        updatetask1.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(updatetask1);
        System.out.println(taskManager.getTasks());

        Epic epic1 = new Epic("epic1", "задача эпика1", Status.NEW);


        final Integer epicId1 = taskManager.addNewEpic(epic1);
        System.out.println(taskManager.getEpics());

        Subtask subtask1 = new Subtask("субстаск1 эпика1", "задача субтаска1", Status.NEW, 34, LocalDateTime.now(), epicId1);
        Subtask subtask2 = new Subtask("субстаск2 эпика1", "задача субтаска2", Status.NEW, 15, LocalDateTime.now(), epicId1);
        Subtask subtask3 = new Subtask("субстаск3 эпика1", "задача субтаска3", Status.NEW, 15, LocalDateTime.now(), epicId1);

        final Integer subtaskId1 = taskManager.addNewSubtask(subtask1);
        final Integer subtaskId2 = taskManager.addNewSubtask(subtask2);
        final Integer subtaskId3 = taskManager.addNewSubtask(subtask3);
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpic(epicId1).getStringStartTime());
        System.out.println(taskManager.getEpic(epicId1).getStringEndTime());
        Subtask updateSubtask1 = new Subtask("обновленный субстаск1 эпика1", "задача субтаска1",
                Status.DONE, 34, LocalDateTime.now(), epicId1);
        updateSubtask1.setId(subtaskId1);
        updateSubtask1.setName("обновленный субстаск1 эпика1");
        updateSubtask1.setDescription("задача субтаска1");
        updateSubtask1.setStatus(Status.DONE);
        updateSubtask1.setStringStartTime("11.01.2023,22:36");
        taskManager.updateSubtask(updateSubtask1);
        System.out.println("-------------------//-------------------------");
        System.out.println(taskManager.getSubtask(subtaskId1));
        System.out.println("-------------------//-------------------------");

//        Subtask updateSubtask2 = taskManager.getSubtask(subtaskId2);
//        updateSubtask2.setName("обновленный субстаск2 эпика1");
//        updateSubtask2.setDescription("задача субтаска2");
//        updateSubtask2.setStatus(Status.DONE);
//        taskManager.updateSubtask(updateSubtask2);

        System.out.println("-------------------//-------------------------");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("-------------------//-------------------------");

        System.out.println(taskManager.getSubtasks());
        Epic updateEpic1 = taskManager.getEpic(epicId1);
        updateEpic1.setName("обновленный epic1");
        taskManager.updateEpic(updateEpic1);

        System.out.println(taskManager.getEpicSubtasks(epicId1));
        System.out.println("-------------------//-------------------------");

        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(2));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1).getStringStartTime());
        System.out.println("-------------------//-------------------------");

        Task task2 = new Task("таск1", "задача таска1", Status.NEW);
        final Integer taskId2 = taskManager.addNewTask(task2);
        System.out.println(taskManager.getTask(taskId2));
        System.out.println("-------------------//-------------------------");
        System.out.println(taskManager.getHistory());
        System.out.println("-------------------//-------------------------");
        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(
                new File("src/resource/historyTasksManager.csv"));
        System.out.println(taskManager1.getHistory());
        System.out.println(taskManager1.getEpic(2).getStringEndTime());
        System.out.println(taskManager.getPrioritizedTasks());

    }
}
