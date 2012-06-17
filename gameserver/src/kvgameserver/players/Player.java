package kvgameserver.players;

import java.io.IOException;

public abstract class Player {

	public String name = null;

	public String offerFrom = null;
	public abstract void send(String message) throws IOException;
	public abstract String receive() throws IOException;
	public abstract boolean hasIncoming() throws IOException;
}
