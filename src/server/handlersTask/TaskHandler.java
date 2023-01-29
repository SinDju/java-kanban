package server.handlersTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fileManager.TaskManager;
import model.Epic;
import model.Task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler implements HttpHandler {
    TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new Gson();
    String response;

    public TaskHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = String.valueOf(exchange.getRequestURI());

        System.out.println("Обрабатывается запрос " + path + " с методом " + method);
        switch (method) {
            case "GET":
                getTask(exchange);
                break;
            case "POST":
                addTask(exchange);
                break;
            case "DELETE":
                deleteTask(exchange);
                break;
            default:
                writeResponse(exchange, "Такой операции не существует", HttpURLConnection.HTTP_NOT_FOUND);
        }
    }

    private void getTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getTasks());
            writeResponse(exchange, response, HttpURLConnection.HTTP_CREATED);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        int id = getTaskId(exchange).get();
        for (Task task : taskManager.getTasks()) {
            if (task.getId().equals(id)) {
                response = gson.toJson(taskManager.getTask(id));
                writeResponse(exchange, response, HttpURLConnection.HTTP_OK);
                return;
            }
        }
        writeResponse(exchange, "Задач с таким id не найдено!", HttpURLConnection.HTTP_NOT_FOUND);

    }


    private void addTask(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(jsonTask, Task.class);
            if (task == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            }
            for (Task task1 : taskManager.getTasks()) {
                if (task.getId() != null && task1.getId().equals(task.getId())) {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Такая задача существует и была обновлена",
                            HttpURLConnection.HTTP_OK);
                    return;
                }
            }
            taskManager.addNewTask(task);
            writeResponse(exchange, "Задача успешно добавлена!",  HttpURLConnection.HTTP_CREATED);

        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteTasks();
            writeResponse(exchange, "Задачи удалены!", HttpURLConnection.HTTP_OK);
        }
        if (getTaskId(exchange).isEmpty()) {
            return;
        }
        int id = getTaskId(exchange).get();
        for (Task task : taskManager.getTasks()) {
            if (task.getId().equals(id)) {
                taskManager.deleteTask(id);
                writeResponse(exchange, "Задача успешно удалена!", HttpURLConnection.HTTP_NOT_FOUND);
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
