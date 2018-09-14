package com.smartDoor.shared;

import java.io.Serializable;

public class LoginInfo implements Serializable{

    public String username;
    public String password;
    public boolean isAdmin;
    public String rfidCode;

    public LoginInfo (String username, String password, boolean isAdmin, String rfidCode) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.rfidCode = rfidCode;
    }

    public LoginInfo () {

    }

    public String getusername () {
        return username;
    }

    public String getpassword () {
        return password;
    }

    public boolean getisAdmin () {
        return isAdmin;
    }

    public String getrfidCode () {
        return rfidCode;
    }


}



