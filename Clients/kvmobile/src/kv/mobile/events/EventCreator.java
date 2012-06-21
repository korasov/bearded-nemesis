package kv.mobile.events;

import kv.mobile.protocols.Receiver;

public class EventCreator implements Runnable {

	private Receiver receiver = null;
	private EventManager manager = null;

	public EventCreator(Receiver receiver, EventManager manager) {
		this.receiver = receiver;
		this.manager = manager;
	}
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			if (receiver.ready()) {
				manager.addMessage(receiver.receive());
			}
		}
	}

}
