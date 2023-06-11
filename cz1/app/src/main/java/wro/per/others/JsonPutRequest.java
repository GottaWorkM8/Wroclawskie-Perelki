package wro.per.others;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonPutRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = JsonPutRequest.class.getSimpleName();

    private String json;
    private String url;

    public JsonPutRequest(String json, String url) {
        this.json = json;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = null;
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send the JSON as the request body
            BufferedOutputStream outputStream = new BufferedOutputStream(conn.getOutputStream());
            outputStream.write(json.getBytes());
            outputStream.flush();

            // Read the response
            InputStream inputStream = conn.getInputStream();
            result = convertInputStreamToString(inputStream);

            // Close the connections
            outputStream.close();
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Handle the response here
        Log.d(TAG, "Response: " + result);
    }
}
