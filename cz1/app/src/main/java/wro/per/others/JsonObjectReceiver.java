package wro.per.others;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonObjectReceiver extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "JsonReceiver";
    private JsonReceiverListener listener;

    public JsonObjectReceiver(JsonReceiverListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        if (urls == null || urls.length == 0) {
            return null;
        }

        String urlString = urls[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return new JSONObject(buffer.toString());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error receiving JSON: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage());
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (listener != null) {
            listener.onJsonReceived(jsonObject);
        }
    }

    public interface JsonReceiverListener {
        void onJsonReceived(JSONObject jsonObject);
    }
}
