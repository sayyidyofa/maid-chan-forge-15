package me.lazydev.projects.maidchanforge.util.db.sqlite;

import me.lazydev.projects.maidchanforge.models.PlayerLog;
import me.lazydev.projects.maidchanforge.util.Reference;
import me.lazydev.projects.maidchanforge.util.helper.Callbacks;
import me.lazydev.projects.maidchanforge.util.helper.Helpers;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class SQLiteHandler {
    private Connection connection = null;
    private Statement statement = null;

    public SQLiteHandler(boolean isSeeding) {
        try {
            this.connection = DriverManager.getConnection(Reference.SQLITE_URL);
            this.statement = this.connection.createStatement();
            this.initializeTable();
            if (isSeeding) {
                this.insertDummy();
                System.out.println(this.getDummy());
            }
        }
        catch (SQLException exception) {
            System.out.println("FATAL: Connecting to SQLite database failed!");
            exception.printStackTrace();
        }
    }

    public void initializeTable() {
        try {
            this.statement.executeUpdate(
                    "create table if not exists playerlog(" +
                            "id integer primary key autoincrement, " +
                            "player_name string," +
                            "time_stamp string," +
                            "player_action string," +
                            "ip_address string" +
                            ")"
            );
            /*ResultSetMetaData metaData = this.statement.executeQuery("select * from playerlog").getMetaData();
            for (int i=1;i<=5;i++) {
                System.out.println("playerlog." + metaData.getColumnName(i));
            }*/
        } catch (SQLException exception) {
            System.out.println("FATAL: Table initialization failed!");
            exception.printStackTrace();
        }
    }

    public void storeAction(String playerName, Date timestamp, String action, String remoteAddress) {
        try {
            this.connection.prepareStatement(
                    String.format("insert into playerlog values(null, \"%1$s\", \"%2$s\", \"%3$s\", \"%4$s\")",
                            playerName, timestamp.toString(), action, remoteAddress)
            ).executeUpdate();
        } catch (SQLException exception) {
            System.out.println("WARNING: Value insertion failed!");
            exception.printStackTrace();
        }
    }

    public String getAllStatus() {
        try {
            ResultSet rows = this.connection.prepareStatement("select * from playerlog").executeQuery();
            Map<Integer, PlayerLog> rowsMap = new HashMap<>();
            while (rows.next()) {
                rowsMap.put(
                        Integer.valueOf(rows.getString("id")),
                        new PlayerLog(
                                rows.getString("player_name"),
                                Helpers.calendarToDate(rows.getString("time_stamp")),
                                rows.getString("player_action"),
                                rows.getString("ip_address")
                        )
                );
            }
            return Helpers.serialize(rowsMap);
        } catch (SQLException exception) {
            System.out.println("WARNING: Value selection failed!");
            exception.printStackTrace();
        }
        return null;
    }

    public String getPlayerStats(String playerName) {
        try {
            ResultSet rows = this.connection.prepareStatement(String.format("select * from playerlog where player_name=\"%s\"", playerName)).executeQuery();
            Map<Integer, PlayerLog> rowsMap = new HashMap<>();
            while (rows.next()) {
                rowsMap.put(
                        Integer.valueOf(rows.getString("id")),
                        new PlayerLog(
                                rows.getString("player_name"),
                                Helpers.calendarToDate(rows.getString("time_stamp")),
                                rows.getString("player_action"),
                                rows.getString("ip_address")
                        )
                );
            }
            return Helpers.serialize(rowsMap);
        } catch (SQLException exception) {
            System.out.println("WARNING: Value selection failed!");
            exception.printStackTrace();
        }
        return null;
    }

    public String getLatestPlayerStatus(String playerName) {
        try {
            ResultSet row = this.connection.prepareStatement(String.format("select * from playerlog where player_name=\"%s\" order by time_stamp desc limit 1", playerName)).executeQuery();
            if (row.next()) {
                return Helpers.serialize(
                        new PlayerLog(
                                row.getString("player_name"),
                                Helpers.calendarToDate(row.getString("time_stamp")) ,
                                row.getString("player_action"),
                                row.getString("ip_address")
                        )
                );
            }
        } catch (SQLException exception) {
            System.out.println("WARNING: Value selection failed!");
            exception.printStackTrace();
        }
        return null;
    }

    public void insertDummy() {
        this.insertDummy(10);
    }

    public void insertDummy(int rows) {
        this.storeAction("andi", new Date(), "login", "127.0.0.1");
        this.storeAction("yudi", new Date(), "login", "127.0.0.1");
        this.storeAction("bendi", new Date(), "login", "127.0.0.1");
    }

    public List<List<String>> getDummy() {
        try {
            return Helpers.resultSetToList(
                    this.connection.prepareStatement("select * from playerlog").executeQuery(),
                    Helpers.arrayToHashMap(new Object[][]{
                            {"id"},
                            {"player_name"},
                            {"time_stamp", (Callbacks) object -> Helpers.calendarToDate((String) object)},
                            {"ip_address"}
                    })
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
