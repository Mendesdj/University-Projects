package com.smartDoor.server;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.smartDoor.shared.Entry;
import com.smartDoor.shared.LoginInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Utils {

    private static final String TAG = "Utils";

    public static boolean writeToAccounts (Context context, LoginInfo account){

        String path = context.getFilesDir().getPath() + "/" + "Accounts.json";


        try{

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

            List<LoginInfo> list = new ArrayList<>();

            try {

                TypeReference<List<LoginInfo>> mapType = new TypeReference<List<LoginInfo>>() {};
                list = mapper.readValue(file, mapType);

            }
            catch (Exception o){

            }

            list.add(account);
            writer.writeValue(file, list);
            return true;

        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean removeFromAccounts (Context context, String username){

        String path = context.getFilesDir().getPath() + "/" + "Accounts.json";


        try{

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

            List<LoginInfo> list = new ArrayList<>();

            try {

                TypeReference<List<LoginInfo>> mapType = new TypeReference<List<LoginInfo>>() {};
                list = mapper.readValue(file, mapType);

            }
            catch (Exception o){

            }

            int indexToRemove = -1;
            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getusername().equals(username)){
                    indexToRemove = i;
                }
            }
            if (indexToRemove != -1 ){
                list.remove(indexToRemove);
            }

            writer.writeValue(file, list);

            if (indexToRemove != -1) {
                return true;
            }
            else {
                return false;
            }

        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static List<LoginInfo> loadUsers(Context context) {

        String path = context.getFilesDir().getPath() + "/" + "Accounts.json";
        List<LoginInfo> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<LoginInfo>> mapType = new TypeReference<List<LoginInfo>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){
                o.printStackTrace();
            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LoginInfo isValidUser (Context context, String username, String password) {

        List<LoginInfo> list = loadUsers(context);

        for (int i = 0 ; i < list.size(); i++) {

            LoginInfo user = list.get(i);

            if (user.getusername().equals(username) && user.getpassword().equals(password)){
                return user;
            }
        }

        return null;

    }


    public static LoginInfo canOpenDoor (Context context, String rfidCode) {

        List<LoginInfo> list = loadUsers(context);

        for (int i = 0 ; i < list.size(); i++) {

            LoginInfo user = list.get(i);

            if (user.getrfidCode().equals(rfidCode)){
                return user;
            }
        }

        return null;

    }

    public static boolean registerEntry (Context context, Entry entry){

        String path = context.getFilesDir().getPath() + "/" + "Entries.json";


        try{

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

            List<Entry> list = new ArrayList<>();

            try {

                TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {};
                list = mapper.readValue(file, mapType);

            }
            catch (Exception o){

            }

            list.add(entry);
            writer.writeValue(file, list);
            return true;

        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static List<Entry> getAllEntries(Context context) {

        String path = context.getFilesDir().getPath() + "/" + "Entries.json";
        List<Entry> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){
                o.printStackTrace();
            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Entry> getUserEntries(Context context, String username) {

        String path = context.getFilesDir().getPath() + "/" + "Entries.json";
        List<Entry> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<Entry>> mapType = new TypeReference<List<Entry>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){
                o.printStackTrace();
            }

            List<Entry> returnList = new ArrayList<>();
            for (int i = 0; i < list.size(); i ++) {
                Entry currentEntry = list.get(i);
                if (currentEntry.getusername().equals(username)) {
                    returnList.add(currentEntry);
                }
            }

            return returnList;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUsernameFromRfidCode(Context context, String rfidcode) {

        String path = context.getFilesDir().getPath() + "/" + "Accounts.json";
        List<LoginInfo> list = new ArrayList<>();

        try {

            File file = new File(path);
            if(!file.exists())
                file.createNewFile();

            ObjectMapper mapper = new ObjectMapper();

            try{
                TypeReference<List<LoginInfo>> mapType = new TypeReference<List<LoginInfo>>() {};
                list = mapper.readValue(file, mapType);
            }
            catch (Exception o){
                o.printStackTrace();
            }

            for (int i = 0; i < list.size(); i++) {
                LoginInfo info = list.get(i);
                if (info.getrfidCode().equals(rfidcode)) {
                    return info.getusername();
                }
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}