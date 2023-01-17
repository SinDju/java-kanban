package fileManager;

import java.nio.file.Path;

public class Managers {



    public static HistoryManager getDefaultHistory() {
        //  возвращает объект InMemoryHistoryManager — историю просмотров.
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(Path path) {
//        возвращает объект FileBackedTasksManager
        return new FileBackedTasksManager(path);
    }

}
