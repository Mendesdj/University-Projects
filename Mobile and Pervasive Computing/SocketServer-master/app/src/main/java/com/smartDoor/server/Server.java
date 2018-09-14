package com.smartDoor.server;

import com.smartDoor.shared.Entry;
import com.smartDoor.shared.LoginInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class Server {
	MainActivity activity;
	ServerSocket serverSocket;
	String message = "";

	static final int socketServerPORT = 8080;

	// Request types
	static final byte LOGIN_ATTEMPT = 1;
	static final byte REGISTER_ATTEMPT = 2;
	static final byte REMOVE_ATTEMPT = 3;
	static final byte ADMIN_GET_ENTRYS = 4;
	static final byte USER_GET_ENTRYS = 5;
	static final byte ADMIN_GET_USERS = 6;
    static final byte DOOR_OPEN = 7;


	public Server(MainActivity activity) {
		this.activity = activity;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	public int getPort() {
		return socketServerPORT;
	}

	public void onDestroy() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class SocketServerThread extends Thread {

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(socketServerPORT);
                String username;
                String password;
                boolean isAdmin;
                String rfidCode;
                boolean enter;

				while (true) {
					Socket socket = serverSocket.accept();

					DataInputStream dIn = new DataInputStream(socket.getInputStream());

					byte code = dIn.readByte();

					switch (code) {
                        case LOGIN_ATTEMPT:
							username = dIn.readUTF();
							password = dIn.readUTF();

                            SocketServerReplyLogin socketServerReplyLogin = new SocketServerReplyLogin(socket, username, password);
                            socketServerReplyLogin.run();

                            break;

						case REGISTER_ATTEMPT:
							username = dIn.readUTF();
							password = dIn.readUTF();
							isAdmin = dIn.readBoolean();
							rfidCode = dIn.readUTF();

                            SocketServerReplyRegister socketServerReplyRegister = new SocketServerReplyRegister(socket, username, password, isAdmin, rfidCode);
                            socketServerReplyRegister.run();

							break;


						case REMOVE_ATTEMPT:
							username = dIn.readUTF();

							SocketServerReplyRemove socketServerReplyRemove = new SocketServerReplyRemove(socket, username);
							socketServerReplyRemove.run();

							break;

                        case ADMIN_GET_ENTRYS:

                            SocketServerReplyAdminGetEntries adminEntries = new SocketServerReplyAdminGetEntries(socket);
                            adminEntries.run();
                            break;

                        case USER_GET_ENTRYS:

                            username = dIn.readUTF();
                            SocketServerReplyUserGetEntries socketServerReplyUserGetEntries = new SocketServerReplyUserGetEntries(socket, username);
                            socketServerReplyUserGetEntries.run();
                            break;

                        case ADMIN_GET_USERS:

                            SocketServerReplyAdminGetUsers socketServerReplyAdminGetUsers = new SocketServerReplyAdminGetUsers(socket);
                            socketServerReplyAdminGetUsers.run();
                            break;

                        case DOOR_OPEN:

                            rfidCode = dIn.readUTF();
                            enter = dIn.readBoolean();
                            SocketServerReplyOpenDoor socketServerReplyOpenDoor = new SocketServerReplyOpenDoor(socket, rfidCode, enter);
                            socketServerReplyOpenDoor.run();
                            break;


                        default:
                            break;
                    }

					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							activity.msg.setText(message);
						}
					});

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class SocketServerReplyLogin extends Thread {

		private Socket hostThreadSocket;
        private String username;
        private String password;

        SocketServerReplyLogin(Socket socket, String username, String password) {
			hostThreadSocket = socket;
			this.username = username;
			this.password = password;
		}

		@Override
		public void run() {
			OutputStream outputStream;

			try {

                LoginInfo info = Utils.isValidUser(activity.getApplicationContext(), username, password);
				outputStream = hostThreadSocket.getOutputStream();
                ObjectOutputStream outStream = new ObjectOutputStream(outputStream);

				if (info != null) {

				    outStream.writeBoolean(true);
					outStream.writeObject(info);
                    outStream.flush();

					message += "Sent LoginInfo " + info.getusername() + "\n";

				}
				else {
                    outStream.writeBoolean(false);

					message += "Login Attempt Declined \n";

				}

				outStream.close();

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message += "Something wrong! " + e.toString() + "\n";
			}

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.msg.setText(message);
				}
			});
		}

	}

    private class SocketServerReplyRegister extends Thread {

        private Socket hostThreadSocket;
        private LoginInfo user;

        SocketServerReplyRegister(Socket socket, String username, String password, boolean Admin, String rfidCode) {
            hostThreadSocket = socket;
            user = new LoginInfo(username, password, Admin, rfidCode);
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {

                boolean registerSuccess = Utils.writeToAccounts(activity.getApplicationContext(), user);
                outputStream = hostThreadSocket.getOutputStream();
                DataOutputStream dOutStream = new DataOutputStream(outputStream);

                if (registerSuccess) {

                    dOutStream.writeBoolean(true);
                    dOutStream.flush();
                    dOutStream.close();

                    message += "User " + user.getusername() + " successfully registered \n";


                }
                else {
                    dOutStream.writeBoolean(false);
                    dOutStream.flush();
                    dOutStream.close();

                    message += "Failed to Register a New User \n";


                }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }


    private class SocketServerReplyRemove extends Thread {

        private Socket hostThreadSocket;
        private String username;

        SocketServerReplyRemove(Socket socket, String username) {
            hostThreadSocket = socket;
            this.username = username;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {

                boolean removeSuccess = Utils.removeFromAccounts(activity.getApplicationContext(), username);
                outputStream = hostThreadSocket.getOutputStream();
                DataOutputStream dOutStream = new DataOutputStream(outputStream);

                if (removeSuccess) {

                    dOutStream.writeBoolean(true);
                    dOutStream.flush();
                    dOutStream.close();

                    message += "User " + username + " successfully removed \n";


                }
                else {
                    dOutStream.writeBoolean(false);
                    dOutStream.flush();
                    dOutStream.close();

                    message += "Failed to Remove User "+ username + " \n";

                }

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

    private class SocketServerReplyAdminGetEntries extends Thread {

        private Socket hostThreadSocket;

        SocketServerReplyAdminGetEntries(Socket socket) {
            hostThreadSocket = socket;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {

                List<Entry> list = Utils.getAllEntries(activity.getApplicationContext());
                outputStream = hostThreadSocket.getOutputStream();
                ObjectOutputStream outStream = new ObjectOutputStream(outputStream);

                if (list != null) {

                    outStream.writeBoolean(true);
                    outStream.writeObject(list);
                    outStream.flush();

                    message += "Sent All Entries List \n";

                }
                else {
                    outStream.writeBoolean(false);

                    message += "Error Fetching Entries List \n";

                }

                outStream.close();

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

    private class SocketServerReplyUserGetEntries extends Thread {

        private Socket hostThreadSocket;
        String username;

        SocketServerReplyUserGetEntries(Socket socket, String username) {
            hostThreadSocket = socket;
            this.username = username;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {

                List<Entry> list = Utils.getUserEntries(activity.getApplicationContext(), username);
                outputStream = hostThreadSocket.getOutputStream();
                ObjectOutputStream outStream = new ObjectOutputStream(outputStream);

                if (list != null) {

                    outStream.writeBoolean(true);
                    outStream.writeObject(list);
                    outStream.flush();

                    message += "Sent User Entries List \n";

                }
                else {
                    outStream.writeBoolean(false);

                    message += "Error Fetching Entries List \n";

                }

                outStream.close();

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

    private class SocketServerReplyAdminGetUsers extends Thread {

        private Socket hostThreadSocket;

        SocketServerReplyAdminGetUsers(Socket socket) {
            hostThreadSocket = socket;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {

                List<LoginInfo> list = Utils.loadUsers(activity.getApplicationContext());
                outputStream = hostThreadSocket.getOutputStream();
                ObjectOutputStream outStream = new ObjectOutputStream(outputStream);

                if (list != null) {

                    outStream.writeBoolean(true);
                    outStream.writeObject(list);
                    outStream.flush();

                    message += "Sent All Users List \n";

                }
                else {
                    outStream.writeBoolean(false);

                    message += "Error Fetching Users List \n";

                }

                outStream.close();

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

    private class SocketServerReplyOpenDoor extends Thread {

        private Socket hostThreadSocket;
        private String rfidCode;
        private boolean enter;

        SocketServerReplyOpenDoor(Socket socket, String rfidCode, boolean enter) {
            hostThreadSocket = socket;
            this.rfidCode = rfidCode;
            this.enter = enter;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {

                LoginInfo user = Utils.canOpenDoor(activity.getApplicationContext(), rfidCode);
                outputStream = hostThreadSocket.getOutputStream();
                ObjectOutputStream outStream = new ObjectOutputStream(outputStream);

                if (user != null) {

                    Entry entry = new Entry(user.getusername(), enter, new Date(), user.getisAdmin());
                    Utils.registerEntry(activity.getApplicationContext(), entry);

                    outStream.writeBoolean(true);
                    outStream.flush();

                    message += "Entry Registered \n";
                }
                else {

                    outStream.writeBoolean(false);
                    outputStream.flush();
                    message += "Failed Entry Register \n";
                }

                outStream.close();

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

	public String getIpAddress() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress
							.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += "Server running at : "
								+ inetAddress.getHostAddress();
					}
				}
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}
		return ip;
	}
}
