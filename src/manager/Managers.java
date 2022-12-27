package manager;

import java.nio.file.Path;

public class Managers {


    public static TaskManager getDefault() {
//        возвращает объект InMemoryTaskManager
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        //  возвращает объект InMemoryHistoryManager — историю просмотров.
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBacked(Path path) {
//        возвращает объект FileBackedTasksManager
        return new FileBackedTasksManager(path);
    }

}
