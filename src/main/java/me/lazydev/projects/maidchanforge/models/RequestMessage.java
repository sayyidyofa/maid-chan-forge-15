package me.lazydev.projects.maidchanforge.models;

public class RequestMessage {
    public String channelString;
    public String playerName;
    public String status;

    public RequestMessage(String channelString) {
        this.channelString = channelString;
    }

    /*public Message(String channelString, String status) {
        this.channelString = channelString;
        this.status = status;
    }*/

    public RequestMessage(String channelString, String playerName) {
        this.channelString = channelString;
        this.playerName = playerName;
    }
}
