package kvgameserver.players;

import java.io.IOException;
import java.util.LinkedList;

import kvgameserver.LoginService;

import org.eclipse.jetty.websocket.WebSocket;

public class WebSocketPlayer extends Player implements WebSocket.OnTextMessage {

	private Connection connection = null;
	private LinkedList<String> messages = null;

	@Override
	public void send(String message) throws IOException {
		connection.sendMessage(message);
	}

	@Override
	public String receive() throws IOException {
		return messages.poll();
	}

	@Override
	public void onClose(int closeCode, String closeMessage) {
		System.out.println(this.name + " closed connection with code "
				+ closeCode + " and message " + closeMessage);
	}

	@Override
	public void onOpen(Connection connection) {
		WebSocketPlayer.this.connection = connection;
		messages = new LinkedList<String>();
		LoginService.login(this);
	}

	@Override
	public void onMessage(String message) {
		 messages.add(message);
	}

	@Override
	public boolean hasIncoming() throws IOException {
		return !messages.isEmpty();
	}

	@Override
	public boolean connected() {
		return connection.isOpen();
	}

}
