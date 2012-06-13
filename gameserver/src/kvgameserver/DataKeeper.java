package kvgameserver;

import java.net.Socket;
import java.util.Hashtable;

import kvgameserver.players.Player;

public class DataKeeper {
	public static Hashtable<String, Player> players =
			new Hashtable<String, Player>();
}
