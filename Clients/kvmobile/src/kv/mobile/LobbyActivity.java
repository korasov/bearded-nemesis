package kv.mobile;

import java.util.concurrent.ConcurrentLinkedQueue;

import kv.mobile.events.Subscriber;
import kv.mobile.protocols.SimpleLobbyComm;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class LobbyActivity extends Activity implements Subscriber {

	private ConcurrentLinkedQueue<String> messages = null;
	private SimpleLobbyComm comm = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lobby);
		messages = new ConcurrentLinkedQueue<String>();
		comm = new SimpleLobbyComm();
		FlightControl.evMan.subscribe(this);
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
		ProcessingUnit checker = new ProcessingUnit();
		checker.execute(null);
	}

	@Override
	public void process(String message) {
		messages.add(message);
	}

	private class ProcessingUnit extends AsyncTask<Object, String, Object> {

		@Override
		protected Object doInBackground(Object... arg0) {
			while (true) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (messages.isEmpty()) {
					continue;
				}
				String message = messages.poll();
				publishProgress(message);
			}
		}
		@Override
		protected void onProgressUpdate(final String... message) {
			String[] parts = message[0].split(":");
			String command = parts[0];
			if (command.equalsIgnoreCase("players") && parts.length == 2) {
				final String[] onlinePlayers = parts[1].split(" ");
				ListView list = (ListView) findViewById(R.id.playerList);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						LobbyActivity.this,
						android.R.layout.simple_list_item_1,
						onlinePlayers);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> av, View v,
							final int pos, long id) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LobbyActivity.this);
						DialogInterface.OnClickListener mbl =
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										comm.opponent(onlinePlayers[pos]);
									}
						};
						String opponent = onlinePlayers[pos];
						builder.setMessage("Do you want to offer a game to " +
								opponent);
						builder.setPositiveButton("Yes", mbl);
						builder.setNegativeButton("No", null);
						builder.show();
					}
				});
				list.invalidate();
			}

			if (command.equalsIgnoreCase("offer")) {
				String opponent = parts[1];
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LobbyActivity.this);
				MessageBoxListener mbl = new MessageBoxListener();
				builder.setMessage(opponent + " offers a game. Want to play?");
				builder.setPositiveButton("Yes", mbl);
				builder.setNegativeButton("No", mbl);
				builder.show();
			}

			if(command.equalsIgnoreCase("rejected")) {
				String opponent = parts[1];
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LobbyActivity.this);
				builder.setMessage(opponent + " rejected your offer");
				builder.setPositiveButton("OK", null);
				builder.show();
			}
		}
	}

	private class MessageBoxListener
									implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean accepted = false;
			switch(which) {
			case DialogInterface.BUTTON_POSITIVE:
				accepted = true;
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				accepted = false;
				break;
			}
			comm.offerResponse(accepted);
		}
		
	}
}
