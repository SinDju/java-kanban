package server.handlersTask;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fileManager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtasksOfEpicHandler implements HttpHandler {
    TaskManager taskManager;
    private final Gson gson = new Gson();
    String response;

    public SubtasksOfEpicHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getEpicSubtasks(exchange);
                break;
            default:
                writeResponse(exchange, "Такой операции не существует", HttpURLConnection.HTTP_NOT_FOUND);

        }

    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        int id = getTaskId(exchange).get();
        for (Epic epics : taskManager.getEpics()) {
            if (epics.getId().equals(id)) {
                response = gson.toJson(taskManager.getEpicSubtasks(id));
                writeResponse(exchange, response, HttpURLConnection.HTTP_OK);
                return;
            }
        }
        writeResponse(exchange, "Задач с таким id не найдено!", HttpURLConnection.HTTP_NOT_FOUND);

    }


    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getQuery().split("=");
        try {
            return Optional.of(Integer.parseInt(pathParts[1]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
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
