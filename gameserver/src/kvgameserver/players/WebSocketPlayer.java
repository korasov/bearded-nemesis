package kvgameserver.players;

import java.io.IOException;

import kvgameserver.Communicator;

import org.eclipse.jetty.websocket.WebSocket;

public class WebSocketPlayer extends Player implements WebSocket.OnTextMessage {

	private Connection connection = null;
	private String lastMessage = null;
	private Object newMessage = new Object();

	@Override
	public void send(String message) throws IOException {
		connection.sendMessage(message);
	}

	@Override
	public String receive() throws IOException {
		try {
			synchronized(newMessage) {
				newMessage.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return lastMessage;
	}

	@Override
	public void onClose(int closeCode, String closeMessage) {
		lastMessage = null;
		newMessage = true;
	}

	@Override
	public void onOpen(Connection connection) {
		WebSocketPlayer.this.connection = connection;
		Communicator comm = new Communicator(this);
		Thread commThread = new Thread(comm);
		commThread.start();
	}

	@Override
	public void onMessage(String message) {
		WebSocketPlayer.this.lastMessage = message;
		synchronized(newMessage) {
			newMessage.notify();
		}
	}

}
