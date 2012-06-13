package kvgameserver.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketPlayer extends Player {

	BufferedReader br = null;
	OutputStream os = null;

	public SocketPlayer(Socket socket) throws IOException {
		br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		os = socket.getOutputStream();
	}

	@Override
	public void send(String message) throws IOException {
		os.write(message.getBytes());
	}

	@Override
	public String receive() throws IOException {
		String message = br.readLine();
		return message;
	}

}
