package kvgameserver;

import kvgameserver.service.Configuration;

public class Main {

	public static void main(String[] args) {
		Configuration.getInstance();
		Thread lobby = new Thread(new Lobby());
		lobby.start();
		Thread sl = new Thread(new SocketListener());
		sl.start();
		Thread wsl = new Thread(new WebSocketListener());
		wsl.start();
	}

}
