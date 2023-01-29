package server.handlersTask;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fileManager.TaskManager;



import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler implements HttpHandler {
    TaskManager taskManager;

    private final Gson gson = new Gson();
    String response;

    public TasksHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getAllTasks(exchange);
                break;
            default:
                writeResponse(exchange, "Такого операции не существует", 404);

        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        if (taskManager.getPrioritizedTasks().isEmpty()) {
            writeResponse(exchange, "Список задач пуст", 200);
        } else {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            writeResponse(exchange, response, 200);
        }
    }

    public static void writeResponse(HttpExchange exchange,
                                     String responseString,
                                     int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

}