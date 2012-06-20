package kv.mobile.events;

public abstract class Event {

	private String args;

	public Event(String args) {
		this.args = args;
	}

	public String getArgs() {
		return this.args;
	}
}
