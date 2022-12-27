package model;

import manager.TasksType;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public TasksType getType() {
        return TasksType.EPIC;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskIds(Integer id) { // ложим id subtaska в лист subtaskIds
        subtaskIds.add(id);
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    public void removeSubtaskIds(Integer id) {
        subtaskIds.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        String result = "Epic{";
        if (subtaskIds == null) {
            result = result + "subtaskIds=" + subtaskIds + ", ";
        } else {
            result = result +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    '}';
        }
        return result;
    }
}
