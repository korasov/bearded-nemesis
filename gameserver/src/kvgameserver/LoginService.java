package kvgameserver;

import java.io.IOException;

import kvgameserver.players.Player;

public class LoginService {

	public static void login(final Player player) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					boolean notLoggedIn = true;
					while(notLoggedIn) {
						if (!player.hasIncoming()) {
							continue;
						}
						String message = player.receive();
						System.out.println(message);
						String[] messageParts = message.split(":");
						if (messageParts[0].trim().toLowerCase().
								equals("conn")) {
							player.name = messageParts[1];
							DataKeeper.lobby.put(player.name, player);
							System.out.println(player.name + " logged in.");
							notLoggedIn = false;
						}
					}
				} catch (IOException ioe) {/*do nothing*/}
			}
		};
		t.start();
	}
}
