package kvgameserver;

import java.net.Socket;
import java.util.Hashtable;

public class DataKeeper {
	public static Hashtable<String, Socket> players =
			new Hashtable<String, Socket>();
}
