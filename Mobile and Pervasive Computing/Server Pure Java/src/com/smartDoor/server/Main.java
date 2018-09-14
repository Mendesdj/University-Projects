package com.smartDoor.server;

public class Main {

	public static void main(String[] args) {

		String soporquesim;
		Server server = new Server();
		System.out.println("Server IP Address: " + server.getIpAddress());
		System.out.println("Server Port: " + server.getPort()); 
		
	}

}
