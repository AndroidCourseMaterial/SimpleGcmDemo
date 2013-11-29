package edu.rosehulman.gcmtutorialandroid;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {

	public static final String TAG = "GCM";
	public static MainActivity mMainActivity = null;
	
	private TextView mDisplay;

	/** GAE Project Number */
	String SENDER_ID = "700226470606"; // for danick-gcm-tutorial

	GoogleCloudMessaging mGcmHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDisplay = (TextView) findViewById(R.id.gcm_simple_strings);
		
		if (getRegistrationId().isEmpty()) {
			mGcmHelper = GoogleCloudMessaging.getInstance(this);
			new GetRegistrationIdTask().execute();
		} else {
			Log.d(TAG, "Alread have a reg id = " + getRegistrationId());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMainActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMainActivity = null;
	}

	private class GetRegistrationIdTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			String msg = "";
			try {
				String registrationId = mGcmHelper.register(SENDER_ID);
				msg = "Device registered, registration ID = " + registrationId;
				storeRegistrationId(registrationId);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {
			Log.d(TAG, msg); // For this simple demo just copy paste this reg id to your backend. (yes:)
		}
	}

	// --------------------- Passing data from BroadcastReceive this Activity ----------------------
	public void receivedGcmJson(JSONObject jsonObj) {
		String simpleString = "No simple string found";
		int intValue = 0;
		double floatValue = 0.0;
		try {
			simpleString = jsonObj.getString("simple_string");
			if (jsonObj.has("int_value")) {
				intValue = jsonObj.getInt("int_value");
			}
			if (jsonObj.has("float_value")) {
				floatValue = jsonObj.getDouble("float_value");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("GCM", "simple_string: " + simpleString);
		Log.d("GCM", "int_value: " + intValue);
		Log.d("GCM", "float_value: " + floatValue);
		
		mDisplay.append(simpleString + "\n");
	}

	// ---------------------- SharedPreferences to store the registration id -----------------------
	public static final String PROPERTY_REG_ID = "registration_id";

	private String getRegistrationId() {
		return getSharedPreferences().getString(PROPERTY_REG_ID, "");
	}

	private void storeRegistrationId(String regId) {
		final SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.commit();
	}

	private SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(this);
		// return getSharedPreferences(MainActivity.class.getSimpleName(),
		// Context.MODE_PRIVATE);
	}
}
