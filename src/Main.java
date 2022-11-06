import Manager.ManagerTask;
import Model.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        ManagerTask managerTask = new ManagerTask();
        Task task1 = new Task("таск1", "задача таска1", Status.NEW);
        managerTask.addNewTask(task1);
        Task updatetask1 = new Task(1,"обнавленный таск1", "задача таска1", Status.IN_PROGRESS);
        System.out.println(managerTask.getTasks());
        managerTask.updateTask(updatetask1);
        System.out.println(managerTask.getTasks());

        Epic epic1 = new Epic("epic1", "задача эпика1", Status.NEW);


        final Integer epicId1 = managerTask.addNewEpic(epic1);
        System.out.println(managerTask.getEpic());

        Subtask subtask1 = new Subtask("субстаск1 эпика1", "задача субтаска1", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("субстаск2 эпика1", "задача субтаска2", Status.NEW, epicId1);
        Subtask subtask3 = new Subtask("субстаск3 эпика1", "задача субтаска3", Status.NEW, epicId1);

        managerTask.addNewSubtask(subtask1);
        managerTask.addNewSubtask(subtask2);
        managerTask.addNewSubtask(subtask3);
        System.out.println(managerTask.getSubtask());
        Subtask updatesubtask2 = new Subtask(4,"обновленный субстаск2 эпика1", "задача субтаска2", Status.DONE, epicId1);
        Subtask updatesubtask1 = new Subtask(3,"обновленный субстаск1 эпика1", "задача субтаска1", Status.DONE, epicId1);
        managerTask.updateSubtask(updatesubtask2);
        managerTask.updateSubtask(updatesubtask1);
        System.out.println(managerTask.getSubtask());

        Epic updateEpic1 = new Epic(epicId1,"обновленный epic1", "задача эпика1", Status.NEW);
        managerTask.updateEpic(updateEpic1);
        managerTask.deleteSubtask(5);
        System.out.println(managerTask.getEpicSubtasks(2));


    }
}
