package model;

import fileManager.TasksType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    protected LocalDateTime endTime;

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(Integer id, String name, String description, Status status, Integer duration, LocalDateTime startTime,
                LocalDateTime endTime) {
        super(id, name, description, status, duration, startTime);
        this.endTime = endTime;
    }

    public Epic(String name, String description, Status status, Integer duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
    public String toString() {
        String result = "Epic{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                ", status=" + status ;
        if (!subtaskIds.isEmpty()) {
            result = result + ", subtaskIds=" + subtaskIds;
        }
        if (startTime != null) {
            result = result +
                    ", duration=" + duration.toMinutes() +
                    ", startTime=" + startTime;
        }

        return result + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, endTime);
    }
}
