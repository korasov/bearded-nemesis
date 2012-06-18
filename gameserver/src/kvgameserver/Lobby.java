package kvgameserver;

import java.io.IOException;
import java.util.Enumeration;

import kvgameserver.games.GameFactory;
import kvgameserver.players.Player;

public class Lobby implements Runnable {

	private static enum Command{players, opponent, offerresponse, nullcommand};

	private static int CYCLE_TIME = 1000;

	private String gameName = "TicTacToe";

	public void run() {
		while (true) {
			Enumeration<Player> players = DataKeeper.lobby.elements();
			int playernum = DataKeeper.lobby.size();
			int pause = CYCLE_TIME / (playernum + 1);
			// + 1 is needed to prevent division by zero
			try {
				Thread.sleep(pause);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while (players.hasMoreElements()) {
				Player player = players.nextElement();
				try {
					Thread.sleep(pause);
					boolean playerReady = player.hasIncoming();
					if (!playerReady) {
						continue;
					}
					String message = player.receive();
					System.out.println(message);
					String[] messageParts = message.split(":");
					String sCommand = messageParts[0].trim().toLowerCase();
					Command command = Command.nullcommand;
					try {
						command = Command.valueOf(sCommand);
					} catch (IllegalArgumentException iae) {/*do nothing*/}
					switch (command) {
					case players:
						returnPlayerList(player);
						break;
					case opponent:
						String opponent = messageParts[1].trim();
						offerNewGame(player, opponent);
						break;
					case offerresponse:
						String answer = messageParts[1].trim();
						processResponse(player, answer);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void processResponse(Player player, String answer)
			throws IOException {
		Player offerer = DataKeeper.lobby.get(player.offerFrom);
		if (answer.equalsIgnoreCase("no")) {
			offerer.send("REJECTED:" + player.name);
		} else if (answer.equalsIgnoreCase("yes")) {
			createNewGame(gameName, player, offerer);
		}
	}

	private void offerNewGame(Player player, String opponent)
			throws IOException {
		Player pOpponent = DataKeeper.lobby.get(opponent);
		/*
		 *  TODO : think about multiple offers. Must be the way to process only
		 *  one offer per main cycle and leave all the others after. Maybe the
		 *  queue of offers, and each player will process its own offers. 
		 */
		pOpponent.offerFrom = player.name;
		pOpponent.send("OFFER:" + player.name);
	}

	private void returnPlayerList(Player player)
			throws IOException {
		StringBuilder playerList = new StringBuilder();
		Enumeration<Player> players = DataKeeper.lobby.elements();
		playerList.append("PLAYERS:");
		while (players.hasMoreElements()) {
			Player current = players.nextElement();
			if (current.name.equals(player.name)) {
				continue;
			}
			playerList.append(current.name);
			playerList.append(" ");
		}
		System.out.println("Sending players list:");
		System.out.println(playerList.toString());
		player.send(playerList.toString());
	}

	private void createNewGame(String gamename, Player player, Player offerer) {
		DataKeeper.lobby.remove(offerer.name);
		DataKeeper.lobby.remove(player.name);
		Runnable game = GameFactory.createGame(gamename, player, offerer);
		Thread gameThread = new Thread(game);
		gameThread.start();
	}

}
