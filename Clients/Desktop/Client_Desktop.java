package DesktopView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client_Desktop {

	private Socket socket = null;
	private BufferedReader br = null;
	private OutputStream os = null;
	private String command = null;
	private String message = null;

	Client_Desktop(Socket socket) {
		this.socket = socket;
	}

	private void openSocket() {
		try {
			os = this.socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			// this.print("Error getting streams from socket. Shutting down.");
			System.exit(1);
		}
	}

	private void closeSocket() {
		try {
			br.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sendMessage(String message) {
		openSocket();
		try {
			os.write(message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setResponse();

	}

	List<String> getPlayerList() {
		List<String> players = new ArrayList<String>();
		if (command == "players") {
			String[] arr = message.split(" ");

			if (arr.length > 0)
				for (int i = 0; i < arr.length; i++) {
					players.add(arr[i]);
				}

		}
		// пока сервер не отдает список. затем удалить
		players.add("Игрок1");
		players.add("Игрок2");
		return players;
	}

	void gameReqest() {
		setResponse();

		if (command == "game_response") {
			if (message == "yes") {
				new GameView();
			} else
				new AlertMessage(this).cancel();
		}

	}

	void setResponse() {
		if (socket.isClosed()) {
			System.out.println("Socket is closed!");
			return;
		}

		String input = null;
		String data = "";

		try {
			while ((data = br.readLine()) != null) {
				data += input + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeSocket();
		String[] parts = null;

		if (data != null) {
			parts = data.split(":");
			command = parts[0].trim().toLowerCase();
			message = parts[1].trim().toLowerCase();
		}

	}
}
