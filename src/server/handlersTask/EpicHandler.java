package server.handlersTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fileManager.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

public class EpicHandler implements HttpHandler {
    TaskManager taskManager;
    private final Gson gson = new Gson();
    String response;

    public EpicHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getEpic(exchange);
                break;
            case "POST":
                addEpic(exchange);
                break;
            case "DELETE":
                deleteEpic(exchange);
                break;
            default:
                writeResponse(exchange, "Такой операции не существует", HttpURLConnection.HTTP_NOT_FOUND);
                //
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getEpics());
            writeResponse(exchange, response,  HttpURLConnection.HTTP_OK);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!",  HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        int id = getTaskId(exchange).get();
        for (Epic epics : taskManager.getEpics()) {
            if (epics.getId().equals(id)) {
                response = gson.toJson(taskManager.getEpic(id));
                writeResponse(exchange, response, HttpURLConnection.HTTP_OK);
                return;
            }
        }
        writeResponse(exchange, "Задач с таким id не найдено!", HttpURLConnection.HTTP_NOT_FOUND);
    }


    private void addEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(jsonTask, Epic.class);
            if (epic == null) {
                writeResponse(exchange, "Задача не должна быть пустой!",
                        HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            }
            for (Epic epics : taskManager.getEpics()) {
                if (epic.getId() != null && epics.getId().equals(epic.getId())) {
                    taskManager.updateEpic(epic);
                    writeResponse(exchange, "Эпик обновлен!", HttpURLConnection.HTTP_OK);
                    return;
                }
            }
            taskManager.addNewEpic(epic);
            writeResponse(exchange, "Задача успешно добавлена!", HttpURLConnection.HTTP_CREATED);

        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON",
                    HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteEpics();
            writeResponse(exchange, "Задачи удалены!", HttpURLConnection.HTTP_OK);
        }
        if (getTaskId(exchange).isEmpty()) {
            return;
        }
        int id = getTaskId(exchange).get();
        for (Epic epics : taskManager.getEpics()) {
            if (epics.getId().equals(id)) {
                taskManager.deleteEpic(id);
                writeResponse(exchange, "Задача успешно удалена!", HttpURLConnection.HTTP_OK);
                return;
            }
        }
        writeResponse(exchange, "Эпиков с таким id не найдено!",  HttpURLConnection.HTTP_NOT_FOUND);
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
