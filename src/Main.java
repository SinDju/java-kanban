import Manager.HistoryManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Manager.TaskManager;
import Model.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("таск1", "задача таска1", Status.NEW);
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

        Subtask subtask1 = new Subtask("субстаск1 эпика1", "задача субтаска1", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("субстаск2 эпика1", "задача субтаска2", Status.NEW, epicId1);
        Subtask subtask3 = new Subtask("субстаск3 эпика1", "задача субтаска3", Status.NEW, epicId1);

        final Integer subtaskId1 = taskManager.addNewSubtask(subtask1);
        final Integer subtaskId2 = taskManager.addNewSubtask(subtask2);
        final Integer subtaskId3 = taskManager.addNewSubtask(subtask3);
        System.out.println(taskManager.getSubtasks());
        Subtask updateSubtask1 = taskManager.getSubtask(subtaskId1);
        updateSubtask1.setName("обновленный субстаск1 эпика1");
        updateSubtask1.setDescription("задача субтаска1");
        updateSubtask1.setStatus(Status.DONE);

        taskManager.updateSubtask(updateSubtask1);

        Subtask updateSubtask2 = taskManager.getSubtask(subtaskId2);
        updateSubtask2.setName("обновленный субстаск2 эпика1");
        updateSubtask2.setDescription("задача субтаска2");
        updateSubtask2.setStatus(Status.DONE);

        taskManager.updateSubtask(updateSubtask2);
        System.out.println(taskManager.getSubtasks());
        Epic updateEpic1 = taskManager.getEpic(epicId1);
        updateEpic1.setName("обновленный epic1");
        taskManager.updateEpic(updateEpic1);

        taskManager.updateEpic(updateEpic1);
        taskManager.deleteSubtask(subtaskId3);
        System.out.println(taskManager.getEpicSubtasks(epicId1));

        System.out.println("-------------------//-------------------------");
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println("-------------------//-------------------------");
        System.out.println(taskManager.getHistory());

    }
}
