package wro.per.others;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendJsonTaskPost extends AsyncTask<String, Void, String> {

    private static final String TAG = SendJsonTaskPost.class.getSimpleName();
    private ResponseListener responseListener;

    public SendJsonTaskPost(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected String doInBackground(String... params) {
        String apiUrl = params[0];
        String jsonData = params[1];
        String response = "";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(jsonData);
            writer.flush();
            writer.close();
            outputStream.close();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                response = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
            } else {
                response = "Error: " + statusCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            response = "Error: " + e.getMessage();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        if (responseListener != null) {
            responseListener.onResponseReceived(result);
        }
    }

    public interface ResponseListener {
        void onResponseReceived(String response);
    }
}
