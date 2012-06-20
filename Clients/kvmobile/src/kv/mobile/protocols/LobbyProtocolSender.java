package kv.mobile.protocols;

public interface LobbyProtocolSender {
	public void players();
	public void opponent(String name);
	public void offerresponse(boolean accept);
}
