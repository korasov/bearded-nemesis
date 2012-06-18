package DesktopView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client_Desktop implements Runnable {

	private BufferedReader br = null;
	private PrintWriter pr = null;
	private String command = null;
	private String message = "";
	private String opponent = null;
	
	private Socket socket = null;
	Command comm=new Command(this);

	// открываем соеденение
	Client_Desktop() {

		try {
			socket = new Socket("127.0.0.1", 10508);
			pr = new PrintWriter(socket.getOutputStream(), true);
			pr.flush();
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			Thread t = new Thread(this);
			t.start();
		} catch (IOException e) {
			System.exit(1);
		}
	}

	void sendMessage(String message) {
		System.out.println("Client sent: " + message);
		pr.println(message);
	}

	
	public void getPlayers() {
		sendMessage("players");
	}

	public String getOpponent() {
		return opponent;
	}

	void gameReqest(String playername) {
		sendMessage("OPPONENT:" + playername);
		opponent = playername;
	}

	void put(String step) {
		sendMessage("PUT:" + step);
	}

	void loggin(String name) {
		sendMessage("CONN:" + name);
		getPlayers();

	}


	// постоянно отслеживаем ответы.
	@Override
	public void run() {

		while (true) {
			String strComamnd=null;
			try {
				strComamnd = br.readLine();
				System.out.println(strComamnd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//распарсили входящее сообщение на комманду и текст
			String[] parts = null;
			 if (strComamnd != null) {
			 parts = strComamnd.split(":");
			 command = parts[0].trim().toLowerCase();
		
			 if (parts.length>1)
			 message = parts[1].trim().toLowerCase();
			 }
					 
			switch (Command.type(command)){
			case Command.PLAYERS:{ comm.getPlayers(message); break;}
			case Command.OFFER:{ comm.offer(message); break;}
			case Command.START:{ comm.initStart(message); break;}
			case Command.LOST:{ comm.status(command); break;}
			case Command.REJECTED:{ comm.rejected(message); break;}
			case Command.PUT: {comm.put(message); System.out.println(message); break;}
			default: { System.out.println("Неизвестная команда");break;}
			
			}
		}
	}

}
