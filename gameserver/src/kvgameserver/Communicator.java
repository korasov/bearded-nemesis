package kvgameserver;

import java.io.IOException;
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

	public void run() {
		try {
			String message = null;
			while((message = player.receive()) != null) {
				String[] parts = message.split(":");
				String command = parts[0].trim().toLowerCase();
				switch (command) {
				case "conn" :
					this.connectPlayer(parts[1].trim());
					break;
				case "opponent" :
					this.offerGame(parts[1].trim());
					break;
				case "players" :
					this.showPlayers();
					break;
				}
			}
			/*
			 * Delete player from player list if connection is lost
			 */
			if (message == null) {
				DataKeeper.players.remove(player.name);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	private void offerGame(String opponentName) throws IOException {
		String offer = "OFFER:" + player.name + "\n";
		Player otherPlayer = DataKeeper.players.get(opponentName);
		if (otherPlayer != null) {
			otherPlayer.send(offer);
		}
	}

	private void connectPlayer(String playerName) throws IOException {
		if (playerName != null) {
			this.player.name = playerName;
			DataKeeper.players.put(playerName, player);
			System.out.println(playerName + " connected.");
			showPlayers();
		}
	}

	private void showPlayers() throws IOException {
		StringBuilder playersOnline = new StringBuilder();
		Set<String> keys = DataKeeper.players.keySet();
		for (String name : keys) {
			if (!name.equals(player.name)) {
				playersOnline.append(name).append(" ");
			}
		}
		String sPlayers = "PLAYERS: " + playersOnline.toString() + "\n";
		player.send(sPlayers);
	}

	private void showGames() throws IOException {
		String sGames = "GAMES: " + games + "\n";
		player.send(sGames);
	}
}
