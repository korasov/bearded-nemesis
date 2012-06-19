package DesktopView;

import java.util.ArrayList;
import java.util.List;

public class Command {
	public static final int PLAYERS = 1;
	public static final int OFFER = 2;
	public static final int START = 3;
	public static final int STATUS = 4;
	public static final int REJECTED = 5;
	public static final int PUT = 6;
	GameView game = null;

	private  Client_Desktop client = null;
	public static List<String> playerList = new ArrayList<String>();

	Command(Client_Desktop client) {
		this.client = client;
	}

	public static int type(String comName) {
		int type = 0;
		if (comName.equals("players")) {
			type = 1;
		}
		if (comName.equals("offer")) {
			type = 2;
		}
		if (comName.equals("start")) {
			type = 3;
		}
		if (comName.equals("win")||comName.equals("lose")||comName.equals("drawn")) {
			type = 4;
		}
		if (comName.equals("rejected")) {
			type = 5;
		}
		if (comName.equals("put")) {
			type = 6;
		}

		return type;
	}

	public void getPlayers(String message) {
		String[] arr = message.trim().split("\\ ");

		if (arr.length > 0 && !arr[0].equals("")) {
			System.out.println(arr[0]);
			for (int i = 0; i < arr.length; i++) {
				playerList.add(arr[i]);
			}
			new ChoiseView(client, playerList);
		} else
			new AlertMessage(client).tryLeate();

	}

	public void offer(String message) {
		new AlertMessage(client).offergame(message);

	}

	public void initStart(String message) {
		if (message.equals("cross")) {
			game = new GameView("cross", client);
		} else
			game = new GameView("zero", client);
	}

	public void status(String message) {
		if (message.equals("win")) {
			new AlertMessage(client).anotherGame(" won :).");
		} else if(message.equals("lost"))
			new AlertMessage(client).anotherGame(" lost :(.");
		else new AlertMessage(client).anotherGame(" played well.");
	}

	public void rejected(String message) {
		new AlertMessage(client).cancel();

	}

	public void put(String message) {
		System.out.println("Сообщение хода"+message);
		game.put(message);
	}

}
