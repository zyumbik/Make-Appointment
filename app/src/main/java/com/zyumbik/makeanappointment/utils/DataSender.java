package com.zyumbik.makeanappointment.utils;

import android.os.AsyncTask;

import com.zyumbik.makeanappointment.data_models.AppointmentData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/** Created by glebsabirzanov on 08/08/16. */

/** Sample code found on SO and adopted for this app
 * I don't know if it works,
 * USE AT YOUR OWN RISK! */

/** Now it just returns 0 or 1 randomly (with higher chance of 1) */

public class DataSender extends AsyncTask <String, String, Integer> {

	private AppointmentData appointmentData;
	private OnTaskFinished listener;

	public DataSender(AppointmentData appointmentData, String url, OnTaskFinished listener) {
		this.appointmentData = appointmentData;
		this.listener = listener;
		this.execute(url);
	}

	public String  performPostCall(String requestURL, JSONObject postData) {

		URL url;
		String response = "";
		try {
			url = new URL(requestURL);

			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setReadTimeout(15000);
			urlConnection.setConnectTimeout(15000);
			urlConnection.setRequestMethod("GET");
			urlConnection.addRequestProperty("Accept", "application/json");
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);

			OutputStream os = urlConnection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getPostDataString(postData));

			writer.flush();
			writer.close();
			os.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				while ((line = br.readLine()) != null) {
					response += line;
				}
			}
			else {
				response="";
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.onError(e.getLocalizedMessage());
		}
		return response;
	}

	@Override
	protected void onPreExecute() {
		listener.onTaskStarted();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Integer s) {
		listener.onSuccess(s);
		super.onPostExecute(s);
	}

	private String getPostDataString(JSONObject data) throws UnsupportedEncodingException {
		return URLEncoder.encode(data.toString(), "UTF-8");
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random random = new Random();
		if (random.nextInt(3) != 1) {
			return 1;
		}
//		try {
//			s = performPostCall(params[0], appointmentData.getAppointmentJSON());
//		} catch (JSONException e) {
//			listener.onError(e.getMessage());
//			e.printStackTrace();
//		}
		return 0;
	}

	public interface OnTaskFinished {
		void onTaskStarted();
		void onSuccess(int status);
		void onError(String errorMessage);
	}

}
