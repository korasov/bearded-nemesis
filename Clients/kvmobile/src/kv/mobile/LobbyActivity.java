package kv.mobile;

import kv.mobile.events.Subscriber;
import kv.mobile.protocols.SimpleLobbyComm;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class LobbyActivity extends Activity implements Subscriber {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lobby);
		FlightControl.evMan.subscribe(this);
		final SimpleLobbyComm comm = new SimpleLobbyComm();
		Button refresh = (Button) findViewById(R.id.refreshLobby);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				comm.players();
			}
		});
		comm.players();
	}

	@Override
	public void process(String message) {
		Log.d("LobbyA", "Processing message " + message);
		String[] parts = message.split(":");
		String command = parts[0];
		if (command.equals("PLAYERS")) {
			Log.d("LobbyA", "Players received");
			String[] onlinePlayers = parts[1].split(" ");
			ListView list = (ListView) findViewById(R.id.playerList);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_list_item_1, onlinePlayers);
			list.setAdapter(adapter);
			list.invalidate();
		}
	}

}
