package server.handlersTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fileManager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtaskHandler implements HttpHandler {
    TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new Gson();
    String response;

    public SubtaskHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getSubTask(exchange);
                break;
            case "POST":
                addSubTask(exchange);
                break;
            case "DELETE":
                deleteSubTask(exchange);
                break;
            default:
                writeResponse(exchange, "Такого операции не существует", 404);
        }
    }

    private void getSubTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getSubtasks());
            writeResponse(exchange, response, 200);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
            return;
        }
        int id = getTaskId(exchange).get();
        for (Subtask subtask : taskManager.getSubtasks()) {
            if (subtask.getId().equals(id)) {
                response = gson.toJson(taskManager.getSubtask(id));
                writeResponse(exchange, response, 200);
                return;
            }
        }
        writeResponse(exchange, "Задач с таким id не найдено!", 404);
    }


    private void addSubTask(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Subtask subTask = gson.fromJson(jsonTask, Subtask.class);
            if (subTask == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", 400);
                return;
            }
            for (Subtask subtask : taskManager.getSubtasks()) {
                if (subTask.getId() != null && subtask.getId().equals(subTask.getId())) {
                    taskManager.updateSubtask(subTask);
                    writeResponse(exchange, "Сабтаск обновлен!", 200);
                    return;
                }
            }
            taskManager.addNewSubtask(subTask);
            writeResponse(exchange, "Задача успешно добавлена!", 201);

        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void deleteSubTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            taskManager.deleteSubtasks();
            writeResponse(exchange, "Задачи успешно удалены!", 200);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            return;
        }
        int id = getTaskId(exchange).get();
        for (Subtask subtask : taskManager.getSubtasks()) {
            if (subtask.getId().equals(id)) {
                taskManager.deleteSubtask(id);
                writeResponse(exchange, "Задача успешно удалена!", 200);
                return;
            }
        }
        writeResponse(exchange, "Задачи с таким id не найдено!", 404);
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
