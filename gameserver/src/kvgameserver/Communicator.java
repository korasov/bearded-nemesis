package kvgameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import kvgameserver.service.Configuration;

public class Communicator implements Runnable {

	private Socket socket = null;
	private BufferedReader br = null;
	private OutputStream os = null;
	private String games = null; 

	public Communicator(Socket socket) {
		this.socket = socket;
		games = Configuration.getInstance().get("games");
		try {
			br = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			os = this.socket.getOutputStream();
		} catch (IOException e) {
			this.print("Error getting streams from socket. Shutting down.");
			System.exit(1);
		}
		
	}

	private void print(String string) {
		System.out.println(string);
	}

	public void run() {
		try {
			String message = null;
			while((message = br.readLine()) != null) {
				String[] parts = message.split(":");
				String command = parts[0].trim().toLowerCase();
				this.print(command);
				switch (command) {
				case "conn" :
					this.connectPlayer(parts[1].trim());
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	private void connectPlayer(String playerName) throws IOException {
		if (playerName != null) {
			DataKeeper.players.add(playerName);
			this.print(playerName + " connected.");
			StringBuilder playersOnline = new StringBuilder();
			for (String name : DataKeeper.players) {
				if (!name.equals(playerName)) {
					playersOnline.append(name).append(" ");
				}
			}
			String sPlayers = "PLAYERS: " + playersOnline.toString() + "\n";
			String sGames = "GAMES: " + games + "\n";
			os.write(sPlayers.getBytes());
			// TODO: uncomment here when multiple games implemented:
			// os.write(sGames.getBytes());
		}
	}
}
