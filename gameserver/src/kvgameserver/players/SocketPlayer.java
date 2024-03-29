package kvgameserver.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketPlayer extends Player {

	private BufferedReader br = null;
	private PrintWriter pw = null;
	private Socket socket = null;

	public SocketPlayer(Socket socket) throws IOException {
		this.socket = socket;
		br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		// "true" above stands for autoflushing the stream
	}

	@Override
	public void send(String message) throws IOException {
		pw.println(message);
	}

	@Override
	public String receive() throws IOException {
		return br.readLine();
	}

	@Override
	public boolean hasIncoming() throws IOException {
		return br.ready();
	}

	@Override
	public boolean connected() {
		return !socket.isClosed();
	}
}
