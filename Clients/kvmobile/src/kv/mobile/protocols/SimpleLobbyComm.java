package kv.mobile.protocols;

import java.io.IOException;

import kv.mobile.FlightControl;

public class SimpleLobbyComm implements Receiver, LobbyProto {

	@Override
	public void players() {
		FlightControl.send("PLAYERS");
	}

	@Override
	public void opponent(String name) {
		FlightControl.send("OPPONENT:" + name);
	}

	@Override
	public void offerResponse(boolean accepted) {
		String answer = accepted ? "yes" : "no";
		FlightControl.send("OFFERRESPONSE:" + answer);
	}

	@Override
	public String receive() {
		String received = null;
		try {
			received = FlightControl.receive();
		} catch (IOException e) {
		}
		return received;
	}

	@Override
	public boolean ready() {
		boolean ready = false;
		try {
			ready = FlightControl.hasIncoming();
		} catch (IOException e) {
		}
		return ready;
	}

}
