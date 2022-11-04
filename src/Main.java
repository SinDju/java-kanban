import Model.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        ManagerTask managerTask = new ManagerTask();
        Task task1 = new Task("таск1", "задача таска1", Status.NEW);
        managerTask.addNewTask(task1);
        System.out.println(managerTask.getTasks());

        Epic epic1 = new Epic("epic1", "задача эпика1", Status.NEW);


        final Integer epicId1 = managerTask.addNewEpic(epic1);
        System.out.println(managerTask.getEpic());

        Subtask subtask1 = new Subtask("субстаск1 эпика1", "задача субтаска1", Status.NEW);
        Subtask subtask2 = new Subtask("субстаск2 эпика1", "задача субтаска2", Status.NEW);


        managerTask.addNewSubtask(subtask1, epicId1);
        managerTask.addNewSubtask(subtask2, epicId1);
        System.out.println(managerTask.getSubtask());

        managerTask.updateSubtask(subtask2, "обновленный субстаск2 эпика1", "задача субтаска2", Status.IN_PROGRESS);
        managerTask.updateSubtask(subtask1, "обновленный субстаск1 эпика1", "задача субтаска1", Status.IN_PROGRESS);
        System.out.println(managerTask.getSubtask());

        managerTask.updateEpic(epic1, "обновленный epic2", "задача эпика2");

        System.out.println(managerTask.getEpic());
        System.out.println(managerTask);
        managerTask.searchForIdEpic(2);
        managerTask.searchForIdSubtasks(3);
        managerTask.searchForIdTask(1);

        managerTask.printSubtasksOfEpic(epic1);
        managerTask.updateStatusOfEpic(epic1);


        managerTask.deleteOfIdEpics(2);
        System.out.println(managerTask);
    }
}
