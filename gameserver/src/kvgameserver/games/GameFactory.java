package kvgameserver.games;

import kvgameserver.players.Player;

public class GameFactory {

	public static Runnable createGame(String name, Player playerOne,
			Player playerTwo) {
		Runnable game = null;
		if (name.equals("TicTacToe")) {
			game = new TicTacToe(playerOne, playerTwo);
		}
		return game;
	}
}
