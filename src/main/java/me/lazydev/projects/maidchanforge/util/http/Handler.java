package me.lazydev.projects.maidchanforge.util.http;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.lazydev.projects.maidchanforge.MaidChanForge;
import me.lazydev.projects.maidchanforge.util.db.sqlite.SQLiteHandler;
import me.lazydev.projects.maidchanforge.util.helper.Helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Handler implements HttpHandler {

    public Handler(String route) throws IOException {

    }
    SQLiteHandler sqLite = new SQLiteHandler(false);

    @Override
    public void handle(HttpExchange httpExchange)/* throws IOException */{
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                this.respond(httpExchange, "Method is not supported!", null);
            case "POST":
                handlePOST(httpExchange, httpExchange.getHttpContext());
            default:

                break;
        }
    }

    private void respond(HttpExchange httpExchange, String resMessage, String resType) /*throws IOException*/ {
        try {
            httpExchange.sendResponseHeaders(200, resMessage.length());
            httpExchange.setAttribute("Content-Type", resType == null? "text/plain" : resType);
            OutputStream os = httpExchange.getResponseBody();
            os.write(resMessage.getBytes());
            os.close();
        } catch (IOException exception) {
            MaidChanForge.LOGGER.error("FATAL: Cannot respond to request!");
        }

    }

    private void handlePOST(HttpExchange httpExchange, HttpContext context) {
        switch (context.getPath()) {
            case "/status/server":
                this.respond(
                        httpExchange,
                        "online",
                        null);
                break;
            case "/status/player/all":
                this.respond(
                        httpExchange,
                        Helpers.serialize(sqLite.getAllStatus()),
                        "text/json"
                );
                break;
            case "/status/player/latest":
                this.respond(
                        httpExchange,
                        Helpers.serialize(sqLite.getLatestPlayerStatus((String) context.getAttributes().get("playerName"))),
                        "text/json"
                );
                break;
            default:
                this.respond(httpExchange, "Invalid request", null);
                break;
        }
    }
}
