package me.lazydev.projects.maidchanforge.util.websocket;

import me.lazydev.projects.maidchanforge.MaidChanForge;
import me.lazydev.projects.maidchanforge.models.RequestMessage;
import me.lazydev.projects.maidchanforge.models.ResponseMessage;
import me.lazydev.projects.maidchanforge.util.db.sqlite.SQLiteHandler;
import me.lazydev.projects.maidchanforge.util.helper.Helpers;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebSocketHandler extends WebSocketServer {

    public WebSocketHandler(int port) {
        super(new InetSocketAddress(port));
        this.start();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Connection established");
        //MaidChanForge.LOGGER.info("Connection opened with: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        RequestMessage request = (RequestMessage) Helpers.deserialize(message, RequestMessage.class);
        SQLiteHandler sqLite = new SQLiteHandler(false);
        switch (request.channelString) {
            case "status/server":
                conn.send(
                        Helpers.serialize(
                                new ResponseMessage(request.channelString, "online")
                        ));
                break;
            case "status/player/all":
                conn.send(
                        Helpers.serialize(
                                new ResponseMessage(request.channelString, Helpers.serialize(sqLite.getAllStatus()))
                        ));
                break;
            case "status/player/latest":
                conn.send(
                        Helpers.serialize(
                                new ResponseMessage(request.channelString, request.playerName, Helpers.serialize(sqLite.getLatestPlayerStatus(request.playerName)))
                        ));
            default:
                conn.send("Invalid message format!");
                break;
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        MaidChanForge.LOGGER.error(String.format("Connection on port %s thrown an error: %s", conn.getLocalSocketAddress(), ex.getMessage()));
    }

    @Override
    public void onStart() {
        MaidChanForge.LOGGER.info("WebSocket server started at address: " + this.getAddress());
    }
}
