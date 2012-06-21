package kv.mobile.events;

public interface Subscriber {
	public void process(String message);
}
