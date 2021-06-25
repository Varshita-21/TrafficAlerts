package com.example.trafficalerts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public static TextView data;
    public static TextView data2;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (TextView) findViewById(R.id.fetch);
        data2 = (TextView) findViewById(R.id.fetch2);
        fetchData process = new fetchData();
        process.execute();
    }
    public class fetchData extends AsyncTask<Void, Void, Void> {
        String data = "";
        public String parsed = "";
        int f = 0;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("https://api.thingspeak.com/channels/1196313/fields/1.json?results");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                JSONObject JO = new JSONObject(data);
                JSONArray JA = JO.getJSONArray("feeds");
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject JO1 = (JSONObject) JA.getJSONObject(i);
                    parsed = JO1.getString("field1");
                }

            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.data2.setText(parsed);
            int k = Integer.parseInt(parsed);
            if (k == 1) {
                Calendar calndr = Calendar.getInstance();
                Date dt=calndr.getTime();
                MainActivity.data.setText("Signal crossed at "+dt);
                addNotify();

            } else {
                MainActivity.data.setText("No alerts.");
            }
        }

    }

    public void addNotify() {

        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}