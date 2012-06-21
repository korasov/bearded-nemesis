package kv.mobile.protocols;

import kv.mobile.FlightControl;

public class SimpleLobbyProtocolSender implements LobbyProtocolSender {

	@Override
	public void players() {
		FlightControl.send("PLAYERS");
	}

	@Override
	public void opponent(String name) {
		FlightControl.send("OPPONENT:" + name);
	}

	@Override
	public void offerresponse(boolean accept) {
		String answer = accept ? "YES" : "NO";
		FlightControl.send("OFFERRESPONSE:" + answer);
	}

}
