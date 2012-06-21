package kv.mobile.events;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import kv.mobile.events.lobbyevents.LobbyEvent;

public class EventManager implements Runnable {

	private ConcurrentLinkedQueue<Event> eventQueue = 
			new ConcurrentLinkedQueue<Event>();
	private ArrayList<LobbySubscriber> lobbySubscribers =
			new ArrayList<LobbySubscriber>();

	public void subscribe(Subscriber newOne) {
		if (newOne instanceof LobbySubscriber) {
			lobbySubscribers.add((LobbySubscriber) newOne);
		}
	}

	public void unsubscribe(Subscriber oldOne) {
		if (oldOne instanceof LobbySubscriber) {
			lobbySubscribers.remove(oldOne);
		}
	}

	public void addEvent(Event e) {
		eventQueue.add(e);
	}

	@Override
	public void run() {
		while(true) {
			try {
				// first we get some sleep
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			while (!eventQueue.isEmpty()) {
				Event event = eventQueue.poll();
				if (event instanceof LobbyEvent) {
					for (LobbySubscriber subs : lobbySubscribers) {
						subs.operate(event);
					}
				}
			}
		}
	}
}
