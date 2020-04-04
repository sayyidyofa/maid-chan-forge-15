package me.lazydev.projects.maidchanforge.models;

public class ResponseMessage {
    public String channelString;
    public String playerName;
    public String status;

    public ResponseMessage(String channelString, String status) {
        this.channelString = channelString;
        this.status = status;
    }

    public ResponseMessage(String channelString, String playerName, String status) {
        this.channelString = channelString;
        this.playerName = playerName;
        this.status = status;
    }
}
