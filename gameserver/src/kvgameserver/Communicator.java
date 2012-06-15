package kvgameserver;

import java.io.IOException;
import java.util.Set;

import kvgameserver.games.GameFactory;
import kvgameserver.players.Player;
import kvgameserver.service.Configuration;

public class Communicator implements Runnable {

	private String games = null;
	private Player player = null;
	private static final String gamename = "TicTacToe";
	// one day this one above will be used
	private enum Command {
		conn,
		opponent,
		players,
		offerresponse,
		nil;
	}

	public Communicator(Player player) {
		this.player = player;
		games = Configuration.getInstance().get("games");		
	}

	public void run() {
		try {
			if (player.name != null) {
				DataKeeper.lobby.put(player.name, player);
			}
			String message = null;
			while((message = player.receive()) != null) {
				String[] parts = message.split(":");
				String sCommand = parts[0].trim().toLowerCase();
				Command command = Command.nil;
				try {
					command = Command.valueOf(sCommand);
				} catch (IllegalArgumentException iae) {
					/*
					 * do nothing. need this construction in case of
					 * unsupported incoming command
					 */ 
				}
				System.out.println(command);
				switch (command) {
				case conn :
					this.connectPlayer(parts[1].trim());
					break;
				case opponent :
					this.offerGame(parts[1].trim());
					break;
				case players :
					this.showPlayers();
					break;
				case offerresponse :
					this.ofrespo(parts[1].toLowerCase().trim());
					break;
				}
				if (!DataKeeper.lobby.contains(player)) {
					break;
				}
			}
			/*
			 * Delete player from player list if connection is lost
			 */
			if (message == null && DataKeeper.lobby.contains(player)) {
				DataKeeper.lobby.remove(player.name);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	private void ofrespo(String answer) {
		Player offerer = DataKeeper.lobby.get(player.offerFrom);
		if (answer.equals("no")) {
			try {
				offerer.send("REJECTED: " + player.name);
			} catch (IOException ioe) {
				//maybe later
			}
		}
		if (answer.equals("yes")) {
			this.startGame(gamename, player, offerer);
		}
	}

	private void offerGame(String opponentName) throws IOException {
		String offer = "OFFER:" + player.name + "\n";
		Player otherPlayer = DataKeeper.lobby.get(opponentName);
		if (otherPlayer != null) {
			otherPlayer.send(offer);
			otherPlayer.offerFrom = player.name;
		}
	}

	private void connectPlayer(String playerName) throws IOException {
		if (playerName != null) {
			this.player.name = playerName;
			DataKeeper.lobby.put(playerName, player);
			System.out.println(playerName + " connected.");
			showPlayers();
		}
	}

	private void showPlayers() throws IOException {
		StringBuilder playersOnline = new StringBuilder();
		Set<String> keys = DataKeeper.lobby.keySet();
		for (String name : keys) {
			if (!name.equals(player.name)) {
				playersOnline.append(name).append(" ");
			}
		}
		String sPlayers = "PLAYERS: " + playersOnline.toString() + "\n";
		player.send(sPlayers);
	}

	@SuppressWarnings("unused")
	private void showGames() throws IOException {
		String sGames = "GAMES: " + games + "\n";
		player.send(sGames);
	}

	private void startGame(String name, Player playerOne, Player playerTwo) {
		System.out.println("Starting a game. Players: " + playerOne.name
				 + " " + playerTwo.name);
		Runnable game = GameFactory.createGame(name, playerOne, playerTwo);
		DataKeeper.lobby.remove(playerOne.name);
		DataKeeper.lobby.remove(playerTwo.name);
		Thread gameThread = new Thread(game);
		gameThread.run();
	}
}
