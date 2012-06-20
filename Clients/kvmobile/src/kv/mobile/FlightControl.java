package kv.mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FlightControl {
	private static Socket connection = null;
	private static BufferedReader br = null;
	private static PrintWriter pw = null;
	
	public static void connect(String addr, int port, String playerName)
			throws Exception {
		connection = new Socket(addr, port);
		br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		pw = new PrintWriter(connection.getOutputStream());
		send("CONN:" + playerName); // need to use protocol here
	}

	public static void send(String message) {
		pw.println(message);
	}
}
