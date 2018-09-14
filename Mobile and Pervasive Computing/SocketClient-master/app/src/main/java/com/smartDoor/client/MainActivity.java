package com.smartDoor.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.smartDoor.shared.Entry;
import com.smartDoor.shared.LoginInfo;
import com.smartDoor.shared.Result;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity  {

	TextView response;
	EditText editTextAddress, editTextPort, editTextUsername, editTextPassword, editTextRfidCode;
	CheckBox isAdminCheckBox;
	Button buttonLogin, buttonRegister, buttonRemove, buttonAdminGetEntries,
			buttonUserGetEntries, buttonAdminGetUsers, buttonOpenDoor;

	static final byte LOGIN_ATTEMPT = 1;
	static final byte REGISTER_ATTEMPT = 2;
	static final byte REMOVE_ATTEMPT = 3;
	static final byte ADMIN_GET_ENTRIES = 4;
	static final byte USER_GET_ENTRIES = 5;
	static final byte ADMIN_GET_USERS = 6;
	static final byte DOOR_OPEN = 7;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		response = (TextView) findViewById(R.id.responseTextView);
		editTextAddress = (EditText) findViewById(R.id.addressEditText);
		editTextPort = (EditText) findViewById(R.id.portEditText);
		editTextUsername = (EditText) findViewById(R.id.username);
		editTextPassword = (EditText) findViewById(R.id.password);
		isAdminCheckBox = (CheckBox) findViewById(R.id.isAdmin);
		editTextRfidCode = (EditText) findViewById(R.id.rfidcode);


		buttonLogin = (Button) findViewById(R.id.loginButton);
		buttonRegister = (Button) findViewById(R.id.registerButton);
		buttonRemove = (Button) findViewById(R.id.removeButton);
		buttonAdminGetEntries = (Button) findViewById(R.id.AdminGetEntries);
		buttonUserGetEntries = (Button) findViewById(R.id.UserGetEntries);
		buttonAdminGetUsers = (Button) findViewById(R.id.AdminGetUsers);
		buttonOpenDoor = (Button) findViewById(R.id.openDoor);

		buttonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), LOGIN_ATTEMPT,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();
				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Success \n";
						LoginInfo info = result.getLoginInfo();
						output += info.getusername() + " " + info.getpassword() + " " +
								info.getrfidCode() + " " + info.getisAdmin();
					}
					else {
						output = "Failed \n";
					}

					response.setText(output);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});

		buttonRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), REGISTER_ATTEMPT,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();

				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Register Succeed \n";
					}
					else {
						output = "Failed Register \n";
					}

					response.setText(output);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}
		});

		buttonRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), REMOVE_ATTEMPT,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();

				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Success Remove \n";
					}
					else {
						output = "Failed Remove \n";
					}

					response.setText(output);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}
		});

		buttonAdminGetEntries.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), ADMIN_GET_ENTRIES,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();

				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Success \n";
						List<Entry> list = result.getEntriesList();
						for (int i = 0; i < list.size(); i++) {
							Entry e = list.get(i);

							output += e.getusername() + " " + e.getdate() + " " +
									e.getenter() + "\n";
						}
					}
					else {
						output = "Failed \n";
					}

					response.setText(output);

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});

        buttonUserGetEntries.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), USER_GET_ENTRIES,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();

				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Success \n";
						List<Entry> list = result.getEntriesList();
						for (int i = 0; i < list.size(); i++) {
							Entry e = list.get(i);

							output += e.getusername() + " " + e.getdate() + " " +
									e.getenter() + "\n";
						}
					}
					else {
						output = "Failed \n";
					}

					response.setText(output);

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});

        buttonAdminGetUsers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), ADMIN_GET_USERS,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();

				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Success \n";

						List<LoginInfo> list = result.getEntriesUser();
						for (int i = 0; i < list.size(); i++) {
							LoginInfo e = list.get(i);

							output += e.getusername() + " " + e.getpassword() + " " +
									e.getrfidCode() + " " + e.getisAdmin() + "\n";
						}

					}
					else {
						output = "Failed \n";
					}

					response.setText(output);

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});

        buttonOpenDoor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Client myClient = new Client(editTextAddress.getText()
						.toString(), Integer.parseInt(editTextPort
						.getText().toString()), DOOR_OPEN,
						editTextUsername.getText().toString(),
						editTextPassword.getText().toString(),
						isAdminCheckBox.isChecked(),
						editTextRfidCode.getText().toString());
				myClient.execute();

				try {
					Result result = myClient.get();
					String output;
					if (result.getSuccess()) {
						output = "Success \n";
					}
					else {
						output = "Failed \n";
					}

					response.setText(output);

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
