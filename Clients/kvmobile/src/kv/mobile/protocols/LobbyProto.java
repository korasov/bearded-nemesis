package kv.mobile.protocols;

public interface LobbyProto {

	public void players();
	public void opponent(String name);
	public void offerResponse(boolean accepted);

}
