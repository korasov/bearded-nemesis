package kvgameserver.games;

import java.io.IOException;

import kvgameserver.Communicator;
import kvgameserver.players.Player;

public class TicTacToe implements Runnable {

	private TTTPlayer playerOne = null;
	private TTTPlayer playerTwo = null;
	private byte[][] field = null;

	private static byte EMPTY = 0;
	private static byte CROSS = 1;
	private static byte ZERO = -1; // oh i am a bad bad boy

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
			playerOne.send("START:CROSS\n");
			playerTwo.send("START:ZERO\n");
			byte winner = EMPTY;
			while (true) {
				playerMove(playerOne, playerTwo);
				if((winner = checkField()) != EMPTY){
					break;
				}
				playerMove(playerTwo, playerOne);
				if((winner = checkField()) != EMPTY){
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
			Player first = playerOne.getPlayer();
			Player second = playerTwo.getPlayer();
			Communicator commfirst = new Communicator(first);
			Communicator commsecond = new Communicator(second);
			Thread tf = new Thread(commfirst);
			Thread ts = new Thread(commsecond);
			tf.start();
			ts.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private byte checkField() {
		byte winner = EMPTY;
		for (int x = 0; x < 3; x++) {
			if ((field[x][1] == field[x][2]) && (field[x][2] == field[x][3])) {
				winner = field[x][1];
			}
		}
		for (int y = 0; y < 3; y++) {
			if ((field[1][y] == field[2][y]) && (field[2][y] == field[3][y])) {
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

	private void playerMove(TTTPlayer from, TTTPlayer to) throws IOException {
		String message = from.receive();
		String[] messageParts = message.split(":");
		String command = messageParts[0].trim();
		String attrs = messageParts[1].trim();
		String[] attrParts = null;
		attrParts = attrs.split(" ");
		int x = Integer.parseInt(attrParts[0]);
		int y = Integer.parseInt(attrParts[1]);
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

		public Player getPlayer() {
			return this.player;
		}
	}
}
