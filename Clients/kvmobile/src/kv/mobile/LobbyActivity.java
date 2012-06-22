package kv.mobile;

import java.util.concurrent.ConcurrentLinkedQueue;

import kv.mobile.events.Subscriber;
import kv.mobile.protocols.SimpleLobbyComm;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class LobbyActivity extends Activity implements Subscriber {

	private ConcurrentLinkedQueue<String> messages = 
			new ConcurrentLinkedQueue<String>();
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
		Button disconnect = (Button) findViewById(R.id.diconnectButton);
		disconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				FlightControl.disconnect();
				finish();
			}
		});
		comm.players();
		AsyncTask checker = new AsyncTask<Object, String, Object>() {
			@Override
			protected Object doInBackground(Object... arg0) {
				while (true) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (messages.isEmpty()) {
						continue;
					}
					String message = messages.poll();
					String[] parts = message.split(":");
					String command = parts[0];
					if (command.equals("PLAYERS") && parts.length == 2) {
						String[] onlinePlayers = parts[1].split(" ");
						publishProgress(onlinePlayers);
					}
				}
			}
			@Override
			protected void onProgressUpdate(final String... players) {
				ListView list = (ListView) findViewById(R.id.playerList);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						LobbyActivity.this,
						android.R.layout.simple_list_item_1, players);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> av, View v,
							int pos, long id) {
						comm.opponent(players[pos]);
					}
				});
				list.invalidate();
			}
		};
		checker.execute(null);
	}

	@Override
	public void process(String message) {
		messages.add(message);
	}

}
