package kv.mobile.events;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

public class EventManager implements Runnable {

	private ConcurrentLinkedQueue<String> messageQueue =
			new ConcurrentLinkedQueue<String>();
	private ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();

	public void subscribe(Subscriber newOne) {
		synchronized(subscribers) {
			subscribers.add(newOne);
		}
	}

	public void unsubscribe(Subscriber oldOne) {
		synchronized(subscribers) {
			subscribers.remove(oldOne);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			while (!messageQueue.isEmpty()) {
				String message = messageQueue.poll();
				synchronized(subscribers) {
					for (Subscriber subscriber : subscribers) {
						subscriber.process(message);
					}
				}
			}
		}
	}

	public void addMessage(String message) {
		messageQueue.add(message);
	}
}
