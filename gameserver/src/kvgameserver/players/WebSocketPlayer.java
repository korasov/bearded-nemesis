package kvgameserver.players;

import java.io.IOException;

import kvgameserver.LoginService;

import org.eclipse.jetty.websocket.WebSocket;

public class WebSocketPlayer extends Player implements WebSocket.OnTextMessage {

	private Connection connection = null;
	private String lastMessage = null;
	private boolean incomingMessage = false;

	@Override
	public void send(String message) throws IOException {
		connection.sendMessage(message);
	}

	@Override
	public String receive() throws IOException {
		return lastMessage;
	}

	@Override
	public void onClose(int closeCode, String closeMessage) {
		lastMessage = null;
	}

	@Override
	public void onOpen(Connection connection) {
		WebSocketPlayer.this.connection = connection;
		LoginService.login(this);
	}

	@Override
	public void onMessage(String message) {
		WebSocketPlayer.this.lastMessage = message;
		incomingMessage = true;
	}

	@Override
	public boolean hasIncoming() throws IOException {
		return incomingMessage;
	}

}
