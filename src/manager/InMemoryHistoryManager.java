package manager;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasksHistory = new LinkedList<>();
   /* Доброго времени суток, Патимат! Прочитала про LinkedList и появилось несколько вопросов:
     Правильно ли я поняла, что LinkedList здесь лучше использовать, так как мы здесь чаще вставляем элементы
     в историю просмотров(это намного быстрее, чем ArrayList), удаляем только 1-й элемент(очень медленно) и получаем
     сразу весь список элементов(очень медленно), но так как у нас всего 10 элементов в списке LinkedList
     он будет немного быстрее, чем ArrayList?
     И еще у нас в ТЗ написано: "В этом спринте возможности трекера ограничены — в истории просмотров допускается
     дублирование и она может содержать только десять задач. В следующем спринте вам нужно будет убрать дубли и
     расширить её размер. " Если в следующем спринте мы расширим историю просмотров, тогда  не станет ли LinkedList
     по быстродействию равен ArrayList?
     */

    @Override
    public void add(Task task) {
        while (tasksHistory.size() > 10) {
            tasksHistory.remove(0);
        }
            tasksHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }

}
