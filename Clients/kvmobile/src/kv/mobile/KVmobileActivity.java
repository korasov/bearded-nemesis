package kv.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class KVmobileActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EditText addressEdit = (EditText) findViewById(
						R.id.serverAddressInput);
				EditText portEdit = (EditText) findViewById(
						R.id.serverPortInput);
				EditText playerNameEdit = (EditText) findViewById(
						R.id.playerNameInput);
				String address = addressEdit.getText().toString();
				String playerName = playerNameEdit.getText().toString();
				int port = Integer.parseInt(portEdit.getText().toString());
				try {
					FlightControl.connect(address, port, playerName);
				} catch (Exception e) {
					AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
							KVmobileActivity.this);
					dlgAlert.setMessage(e.getMessage());
				    dlgAlert.setTitle("Could not connect");
				    dlgAlert.setPositiveButton("OK", null);
				    dlgAlert.setCancelable(true);
				    dlgAlert.create().show();
				}
			}
        	
        });
    }
}