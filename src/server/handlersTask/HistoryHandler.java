package server.handlersTask;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fileManager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class HistoryHandler implements HttpHandler {
    TaskManager taskManager;
    private final Gson gson = new Gson();
    String response;

    public HistoryHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getHistoryList(exchange);
                break;
            default:
                writeResponse(exchange, "Такого операции не существует", HttpURLConnection.HTTP_NOT_FOUND);

        }
    }

    private void getHistoryList(HttpExchange exchange) throws IOException {
        if (taskManager.getHistory().isEmpty()) {
            writeResponse(exchange, "История пуста!", HttpURLConnection.HTTP_OK);
        } else {
            response = gson.toJson(taskManager.getHistory());
            writeResponse(exchange, response, HttpURLConnection.HTTP_OK);
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