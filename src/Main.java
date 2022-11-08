import Manager.ManagerTask;
import Model.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        ManagerTask managerTask = new ManagerTask();
        Task task1 = new Task("таск1", "задача таска1", Status.NEW);
        final Integer taskId1 = managerTask.addNewTask(task1);
        System.out.println(managerTask.getTasks());

        Task updatetask1 = managerTask.getTask(taskId1);
        updatetask1.setName("обнавленный таск1");
        updatetask1.setDescription("задача таска1");
        updatetask1.setStatus(Status.IN_PROGRESS);

        managerTask.updateTask(updatetask1);
        System.out.println(managerTask.getTasks());

        Epic epic1 = new Epic("epic1", "задача эпика1", Status.NEW);


        final Integer epicId1 = managerTask.addNewEpic(epic1);
        System.out.println(managerTask.getEpics());

        Subtask subtask1 = new Subtask("субстаск1 эпика1", "задача субтаска1", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("субстаск2 эпика1", "задача субтаска2", Status.NEW, epicId1);
        Subtask subtask3 = new Subtask("субстаск3 эпика1", "задача субтаска3", Status.NEW, epicId1);

        final Integer subtaskId1 = managerTask.addNewSubtask(subtask1);
        final Integer subtaskId2 = managerTask.addNewSubtask(subtask2);
        final Integer subtaskId3 = managerTask.addNewSubtask(subtask3);
        System.out.println(managerTask.getSubtasks());
        Subtask updateSubtask1 = managerTask.getSubtask(subtaskId1);
        updateSubtask1.setName("обновленный субстаск1 эпика1");
        updateSubtask1.setDescription("задача субтаска1");
        updateSubtask1.setStatus(Status.DONE);

        managerTask.updateSubtask(updateSubtask1);

        Subtask updateSubtask2 = managerTask.getSubtask(subtaskId2);
        updateSubtask2.setName("обновленный субстаск2 эпика1");
        updateSubtask2.setDescription("задача субтаска2");
        updateSubtask2.setStatus(Status.DONE);

        managerTask.updateSubtask(updateSubtask2);
        System.out.println(managerTask.getSubtasks());
        Epic updateEpic1 = managerTask.getEpic(epicId1);
        updateEpic1.setName("обновленный epic1");
        managerTask.updateEpic(updateEpic1);

        managerTask.updateEpic(updateEpic1);
        managerTask.deleteSubtask(subtaskId3);
        System.out.println(managerTask.getEpicSubtasks(epicId1));


    }
}
