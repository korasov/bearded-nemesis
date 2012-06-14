package kvgameserver;

import java.util.Hashtable;

import kvgameserver.players.Player;

public class DataKeeper {
	public static Hashtable<String, Player> lobby =
			new Hashtable<String, Player>();
}
