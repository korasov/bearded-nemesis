package kv.mobile.protocols;

import java.io.IOException;

import kv.mobile.FlightControl;

public class SimpleLobbyProtocolReceiver implements ProtocolReceiver {

	@Override
	public String receive() {
		String answer = null;
		try {
			answer = FlightControl.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public boolean ready() {
		boolean answer = false;
		try {
			answer = FlightControl.hasIncoming();
		} catch (IOException e) {
			/*
			 * nothing to do here. There's nothing to receive if connection is
			 * lost
			 */
		}
		return answer;
	}

}
