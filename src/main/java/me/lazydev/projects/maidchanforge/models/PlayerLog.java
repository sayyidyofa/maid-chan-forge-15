package me.lazydev.projects.maidchanforge.models;

import java.util.Date;

public class PlayerLog {
    public String playerName;
    public Date timestamp;
    public String action;
    public String remoteAddress;

    public PlayerLog(String playerName, Date timestamp, String action, String remoteAddress) {
        this.playerName = playerName;
        this.timestamp = timestamp;
        this.action = action;
        this.remoteAddress = remoteAddress;
    }
}
