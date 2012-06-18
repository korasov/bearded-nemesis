package kvgameserver.games;

import java.io.IOException;
import java.util.Arrays;

import kvgameserver.players.Player;

public class TicTacToe implements Runnable {

	private TTTPlayer playerOne = null;
	private TTTPlayer playerTwo = null;
	private byte[][] field = null;

	private static byte EMPTY = 0;
	private static byte CROSS = 1;
	private static byte ZERO = -1; // oh i am a bad bad boy

	private static int CHARTABLE_START = 0x30;

	public TicTacToe(Player first, Player second) {
		field = new byte[3][3];
		double mark = Math.random();
		if (mark > 0.5) {
			/*
			 * Player "first" gets the cross and makes first move
			 */
			playerOne = new TTTPlayer(first, CROSS);
			playerTwo = new TTTPlayer(second, ZERO);
		} else {
			/*
			 * otherwise Player "second" gets the cross and makes first move
			 */
			playerOne = new TTTPlayer(second, CROSS);
			playerTwo = new TTTPlayer(first, ZERO);
		}
	}

	@Override
	public void run() {
		try {
			playerOne.send("START:CROSS");
			playerTwo.send("START:ZERO");
			byte winner = EMPTY;
			while (true) {
				playerMove(playerOne, playerTwo);
				if((winner = checkField()) != EMPTY) {
					break;
				}
				playerMove(playerTwo, playerOne);
				if((winner = checkField()) != EMPTY) {
					break;
				}
			}
			if (winner == CROSS) {
				playerOne.send("WIN");
				playerTwo.send("LOSE");
			}
			if (winner == ZERO) {
				playerOne.send("LOSE");
				playerTwo.send("WIN");
			}
			//TODO: implement "one more game" ability
			// TODO : return to lobby
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private byte checkField() {
		printField();
		byte winner = EMPTY;
		for (int x = 0; x < 3; x++) {
			if ((field[x][0] == field[x][1]) && (field[x][1] == field[x][2])) {
				winner = field[x][1];
			}
		}
		for (int y = 0; y < 3; y++) {
			if ((field[0][y] == field[1][y]) && (field[1][y] == field[2][y])) {
				winner = field[1][y];
			}
		}
		if ((field[0][0] == field[1][1]) && (field[1][1] == field[2][2])) {
			winner = field[1][1];
		}
		if ((field[2][0] == field[1][1]) && (field[1][1] == field[0][2])) {
			winner = field[1][1];
		}
		return winner;
	}

	private void printField() {
		for (int x = 0; x < field.length; x++) {
			for (int y = 0; y < field[0].length; y++) {
				System.out.print(field[x][y] + "\t");
			}
			System.out.println();
		}
	}

	private void playerMove(TTTPlayer from, TTTPlayer to) throws IOException {
		String message = from.receive();
		String[] messageParts = message.split(":");
		// TODO : should be a check for "put" command
		String attrs = messageParts[1].trim();
		int x = attrs.charAt(0) - CHARTABLE_START;
		int y = attrs.charAt(1) - CHARTABLE_START;
		if (field[x][y] == EMPTY) {
			field[x][y] = from.sign;
			to.send(message);
		} else {
			from.send("NEIN!");
			playerMove(from, to);
		}
	}

	private class TTTPlayer {

		public byte sign = EMPTY;
		private Player player = null;

		public TTTPlayer(Player player, byte sign) {
			this.player = player;
			this.sign = sign;
		}

		public void send(String message) throws IOException {
			player.send(message);
		}

		public String receive() throws IOException {
			return player.receive();
		}

		@SuppressWarnings("unused")
		public Player getPlayer() {
			return this.player;
		}
	}
}
