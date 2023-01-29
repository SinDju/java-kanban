package fileManager;


import server.HTTPTaskManager;
import server.KVServer;

import java.io.IOException;

public class Managers {
    static public TaskManager getDefault(String url) throws IOException, InterruptedException {
        return new HTTPTaskManager(url);
    }
    static public TaskManager getInMemoryTaskManger() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        //  возвращает объект InMemoryHistoryManager — историю просмотров.
        return new InMemoryHistoryManager();
    }

    static public FileBackedTasksManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);
    }
}
