package manager;

public enum TasksEnum {
    TASK("TASK"), SUBTASK("SUBTASK"), EPIC("EPIC");

    private final String tasksEnum;

    TasksEnum(String tasksEnum) {
        this.tasksEnum = tasksEnum;
    }

    public String getTasksEnum() {
        return tasksEnum;
    }
}
