package com.smartDoor.shared;

import java.io.Serializable;
import java.util.Date;

public class Entry implements Serializable{

    public String username;
    public boolean enter;
    public Date date;
    public boolean isAdmin;

    // enter == true ==> enter room
    // enter == false ==> exit room

    public Entry (String username, boolean enter, Date date, boolean isAdmin) {
        this.username = username;
        this.enter = enter;
        this.date = date;
        this.isAdmin = isAdmin;
    }

    public Entry () {

    }

    public String getusername () {
        return username;
    }

    public boolean getenter () {
        return enter;
    }

    public Date getdate () {
        return date;
    }

    public boolean getisAdmin () {
        return isAdmin;
    }

}
