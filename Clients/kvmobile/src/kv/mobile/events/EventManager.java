package kv.mobile.events;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

public class EventManager implements Runnable {

	private ConcurrentLinkedQueue<String> messageQueue =
			new ConcurrentLinkedQueue<String>();
	private ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();

	public void subscribe(Subscriber newOne) {
		Log.d("evMan", "Adding new subscriber" + newOne);
		synchronized(subscribers) {
			Log.d("evMan", "Now in synchronized section");
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
		Log.d("evMan", "Running evMan thread");
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			while (!messageQueue.isEmpty()) {
				String message = messageQueue.poll();
				Log.d("evMan", "Sending message " + message
						+ " to all subcribers");
				synchronized(subscribers) {
					for (Subscriber subscriber : subscribers) {
						Log.d("evMan", "Calling all girls: " + subscriber);
						subscriber.process(message);
					}
				}
			}
		}
	}

	public void addMessage(String message) {
		Log.d("evMan", "Adding message " + message + " to queue");
		messageQueue.add(message);
	}
}
