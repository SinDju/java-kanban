package fileManager;

public enum TasksType {
    TASK("TASK"), SUBTASK("SUBTASK"), EPIC("EPIC");

    private final String tasksType;

    TasksType(String tasksType) {
        this.tasksType = tasksType;
    }

    public String getTasksType() {
        return tasksType;
    }
}
