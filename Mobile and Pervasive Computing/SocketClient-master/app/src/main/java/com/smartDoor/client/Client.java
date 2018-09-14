package com.smartDoor.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import android.os.AsyncTask;

import com.smartDoor.shared.Entry;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

public class Client extends AsyncTask<Void, Void, Result> {

    private String dstAddress;
    private int dstPort;
    private byte code;
    private String username, password,rfidCode;
    private boolean isAdmin;

    // Request types
    static final byte LOGIN_ATTEMPT = 1;
    static final byte REGISTER_ATTEMPT = 2;
    static final byte REMOVE_ATTEMPT = 3;
    static final byte ADMIN_GET_ENTRIES = 4;
    static final byte USER_GET_ENTRIES = 5;
    static final byte ADMIN_GET_USERS = 6;
    static final byte DOOR_OPEN = 7;

    public Client(String addr, int port, byte type, String username, String password, boolean isAdmin, String rfidCode) {
        dstAddress = addr;
        dstPort = port;
        code = type;
        this.username = username;
        this.password = password;
        this.rfidCode = rfidCode;
        this.isAdmin = isAdmin;

    }
    @SuppressWarnings("unchecked")
    @Override
    protected Result doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            InputStream inputStream;
            ObjectInputStream objectInStream;
            DataInputStream dataInputStream;
            boolean success;
            Result result;

            switch (code) {
                case LOGIN_ATTEMPT:
                    dOut.writeByte(code);
                    dOut.writeUTF(username);
                    dOut.writeUTF(password);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    objectInStream = new ObjectInputStream(inputStream);
                    success = objectInStream.readBoolean();
                    result = new Result(success);

                    if (success) {
                        LoginInfo info = (LoginInfo) objectInStream.readObject();
                        objectInStream.close();
                        result.setLoginInfo(info);
                    }

                    return result;

                case REGISTER_ATTEMPT:
                    dOut.writeByte(code);
                    dOut.writeUTF(username);
                    dOut.writeUTF(password);
                    dOut.writeBoolean(isAdmin);
                    dOut.writeUTF(rfidCode);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    dataInputStream = new DataInputStream(inputStream);
                    success = dataInputStream.readBoolean();
                    dataInputStream.close();
                    result = new Result(success);
                    return result;

                case REMOVE_ATTEMPT:
                    dOut.writeByte(code);
                    dOut.writeUTF(username);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    dataInputStream = new DataInputStream(inputStream);
                    success = dataInputStream.readBoolean();
                    dataInputStream.close();
                    result = new Result(success);
                    return result;

                case ADMIN_GET_ENTRIES:

                    dOut.writeByte(code);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    objectInStream = new ObjectInputStream(inputStream);
                    success = objectInStream.readBoolean();
                    result = new Result(success);

                    if (success) {
                        List<Entry> list = (List<Entry>) objectInStream.readObject();
                        objectInStream.close();
                        result.setEntriesList(list);

                    }

                    return result;

                case USER_GET_ENTRIES:

                    dOut.writeByte(code);
                    dOut.writeUTF(username);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    objectInStream = new ObjectInputStream(inputStream);
                    success = objectInStream.readBoolean();
                    result = new Result(success);

                    if (success) {
                        List<Entry> list = (List<Entry>) objectInStream.readObject();
                        objectInStream.close();
                        result.setEntriesList(list);

                    }

                    return result;

                case ADMIN_GET_USERS:

                    dOut.writeByte(code);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    objectInStream = new ObjectInputStream(inputStream);
                    success = objectInStream.readBoolean();
                    result = new Result(success);

                    if (success) {
                        List<LoginInfo> list = (List<LoginInfo>) objectInStream.readObject();
                        result.setEntriesUser(list);
                        objectInStream.close();

                    }

                    return result;

                case DOOR_OPEN:

                    dOut.writeByte(code);
                    dOut.writeUTF(rfidCode);
                    // entry or exit
                    dOut.writeBoolean(true);
                    dOut.flush();

                    inputStream = socket.getInputStream();
                    objectInStream = new ObjectInputStream(inputStream);
                    success = objectInStream.readBoolean();
                    objectInStream.close();

                    result = new Result(success);
                    return result;

                default:

                    return new Result(false);
            }

        } catch (UnknownHostException e) {

            e.printStackTrace();
            return new Result(false);

        } catch (IOException e) {

            e.printStackTrace();
            return new Result(false);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new Result(false);

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Result result) {

        super.onPostExecute(result);

    }

}
