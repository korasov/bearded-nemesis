package kvgameserver.events;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueue {

	private static EventQueue instance = null;
	private Queue<KVEvent> events = new ConcurrentLinkedQueue<KVEvent>();

	public static EventQueue getInstance() {
		if (instance == null) {
			instance = new EventQueue();
		}
		return instance;
	}

	public void put(KVEvent event) {
		events.add(event);
	}

	public KVEvent get() {
		return events.poll();
	}
}
