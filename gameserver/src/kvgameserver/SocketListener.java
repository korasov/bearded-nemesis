package kvgameserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import kvgameserver.players.Player;
import kvgameserver.players.SocketPlayer;
import kvgameserver.service.Configuration;

public class SocketListener implements Runnable{

	@Override
	public void run() {
		String sPort = Configuration.getInstance().get("port");
		int port = Integer.parseInt(sPort);
		System.out.println("Starting socket listener at port " + port + ".");
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Socket listener started.");
			while (true) {
				Socket csocket = ss.accept();
				Player player = new SocketPlayer(csocket);
				LoginService.login(player);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
