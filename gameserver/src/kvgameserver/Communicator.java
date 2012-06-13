package kvgameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;

import kvgameserver.players.Player;
import kvgameserver.service.Configuration;

public class Communicator implements Runnable {

	private String games = null;
	private Player player = null;

	public Communicator(Player player) {
		this.player = player;
		games = Configuration.getInstance().get("games");		
	}

	private void print(String string) {
		System.out.println(string);
	}

	public void run() {
		try {
			String message = null;
			while((message = player.receive()) != null) {
				String[] parts = message.split(":");
				String command = parts[0].trim().toLowerCase();
				this.print(command);
				switch (command) {
				case "conn" :
					this.connectPlayer(parts[1].trim());
					break;
				case "opponent" :
					this.offerGame(parts[1]);
					break;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	private void offerGame(String opponentName) throws IOException {
		String offer = "OFFER:" + player.name + "\n";
		Player otherPlayer = DataKeeper.players.get(opponentName);
		otherPlayer.send(offer);
	}

	private void connectPlayer(String playerName) throws IOException {
		if (playerName != null) {
			DataKeeper.players.put(playerName, player);
			this.print(playerName + " connected.");
			StringBuilder playersOnline = new StringBuilder();
			Set<String> keys = DataKeeper.players.keySet();
			for (String name : keys) {
				if (!name.equals(playerName)) {
					playersOnline.append(name).append(" ");
				}
			}
			String sPlayers = "PLAYERS: " + playersOnline.toString() + "\n";
			String sGames = "GAMES: " + games + "\n";
			player.send(sPlayers);
			// TODO: uncomment here when multiple games implemented:
			// player.send(sGames);
		}
	}
}
