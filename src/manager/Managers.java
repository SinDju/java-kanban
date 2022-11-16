package manager;

public class Managers {


    public static TaskManager getDefault() {
//        возвращает объект InMemoryTaskManager
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        //  возвращает объект InMemoryHistoryManager — историю просмотров.
        return new InMemoryHistoryManager();
    }
}
