package com.smartDoor.shared;

import java.util.List;

public class Result {

    private boolean success;
    private LoginInfo info;
    private List<Entry> entriesList;
    private List<LoginInfo> entriesUser;

    public Result (boolean success) {
        this.success = success;
        this.info = null;
        entriesList = null;
        entriesUser = null;
    }

    public void setLoginInfo (LoginInfo info) {
        this.info = info;
    }

    public void setEntriesList (List<Entry> entriesList) {
        this.entriesList = entriesList;
    }

    public void setEntriesUser (List<LoginInfo> entriesUser) {
        this.entriesUser = entriesUser;
    }

    public boolean getSuccess () {
        return success;
    }

    public LoginInfo getLoginInfo () {
        return info;
    }

    public List<Entry> getEntriesList () {
        return entriesList;
    }

    public List<LoginInfo> getEntriesUser () {
        return entriesUser;
    }
}
