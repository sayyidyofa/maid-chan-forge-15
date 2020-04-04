package me.lazydev.projects.maidchanforge.util.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private String[] routes = {
            "/status/server",
            "/status/player/all",
            "/status/player/latest"
    };

    public Server() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        for (String route: this.routes) {
            server.createContext(route, new Handler(route));
        }
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
