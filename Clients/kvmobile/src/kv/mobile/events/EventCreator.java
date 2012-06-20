package kv.mobile.events;

import kv.mobile.protocols.ProtocolReceiver;

public class EventCreator implements Runnable {

	public EventCreator(ProtocolReceiver pr) {
		this.pr = pr;
	}
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
