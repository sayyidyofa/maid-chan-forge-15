package me.lazydev.projects.maidchanforge.util.bots.line;

import kong.unirest.Unirest;
import me.lazydev.projects.maidchanforge.util.Reference;

public class BotHandler {
    private static final String apiBaseUrl = Reference.LINE_API_BASE_URL;
    public static void broadcast(String message) {
        Unirest.post(apiBaseUrl + "/broadcast")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Reference.LINE_API_KEY)
                .body("{\"messages\":[{\"type\":\"text\",\"text\":\"" + message + "\"}]}")
                .asEmpty();
    }

    public static void push(String message, String userId) {
        Unirest.post(apiBaseUrl + "/push")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Reference.LINE_API_KEY)
                .body("{\"to\":\""+userId+"\",\"messages\":[{\"type\":\"text\",\"text\":\"" + message + "\"}]}")
                .asEmpty();
    }
}
