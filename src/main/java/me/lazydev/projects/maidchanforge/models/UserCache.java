package me.lazydev.projects.maidchanforge.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class UserCache {
    public String name;
    public UUID uuid;
    public Calendar expiresOn;

    public UserCache(String name, UUID uuid, String expiresOn) {
        this.name = name;
        this.uuid = uuid;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(expiresOn));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.expiresOn = (Calendar) cal.clone();
    }

    public UserCache(HashMap<?, ?> map) {
        this.name = (String) map.get("name");
        this.uuid = (UUID) map.get("uuid");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse((String) map.get("expiresOn")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.expiresOn = (Calendar) cal.clone();
    }
}
