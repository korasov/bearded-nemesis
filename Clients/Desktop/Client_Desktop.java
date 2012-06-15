package DesktopView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client_Desktop implements Runnable {

	private Socket socket = null;
	private BufferedReader br = null;
	private OutputStream os = null;
	private String command = null;
	private String message = null;
	private String opponent = null;
	private GameView game = null;

	//открываем соеденение
	Client_Desktop(Socket socket) {
		this.socket = socket;
		try {
			os = this.socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			Thread t= new Thread(this);
			t.start();
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
		System.out.println("Client  "+message);
		try {
			os.write(message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<String> getPlayers() {
		sendMessage("players");

		List<String> players = new ArrayList<String>();
		if (command == "players") {
			String[] arr = message.split(" ");

			if (arr.length > 0)
				for (int i = 0; i < arr.length; i++) {
					players.add(arr[i]);
				}
		}
	
		// пока сервер не отдает список. затем удалить
		//players.add("Игрок1");
		//players.add("Игрок2");
		
		return players;
	}

	public String getOpponent() {
		return opponent;
	}

	void gameReqest(String playername) {
		sendMessage("Opponent:" + playername);
		opponent = playername;

		if (command == "start") {
			if (message == "cross") {
			game=new GameView("cross",this);
			} else
				game=new GameView("zero",this);
		} else if (command == "rejected") {
			new AlertMessage(this).cancel();
		}
		
	}

	void put(String step) {
		sendMessage("PUT:" + step);

		if (command == "status") {
			if (message == "won") {
				new AlertMessage(this).anotherGame("win");
			} else
				new AlertMessage(this).anotherGame("lose");
		}
		
		else if(command == "put") 
			if(message.length()==2) game.put(message);		
	}

	void loggin(String name) {
		sendMessage("CONN:" + name);
		new ChoiseView(this);
	}


//постоянно отслеживаем ответы.
	@Override
	public void run() {
		
		while (true) {
			
			if (socket.isClosed()) {
				System.out.println("Socket is closed!");
				new AlertMessage(this).serverFail();
				return;
			}
			
			String input = null;
			String data = "";
			try {
				while ((data = br.readLine()) != null) {
					data += input + "\n";
					System.out.println("Server"+message);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] parts = null;

			if (data != null) {
				parts = data.split(":");
				command = parts[0].trim().toLowerCase();
				//if (parts[1]!=null)
				message = parts[1].trim().toLowerCase();
			}
			
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		}
	}

