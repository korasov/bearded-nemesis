package kvgameserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Communicator implements Runnable {

	private Socket socket = null;

	public Communicator(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			OutputStream os = socket.getOutputStream();
			while(!socket.isClosed()) {
				String message = br.readLine();
				String respond = "RE: " + message;
				os.write(respond.getBytes());
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

}
