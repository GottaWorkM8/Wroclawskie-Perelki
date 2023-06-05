package wro.per.others;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequestTask extends AsyncTask<Void, Void, String> {

    public interface ApiResponseListener {
        void onApiResponse(String response);
    }

    private String apiUrl;
    private String requestMethod;
    private String requestData;
    private ApiResponseListener listener;

    public ApiRequestTask(String apiUrl, String requestMethod, String requestData, ApiResponseListener listener) {
        this.apiUrl = apiUrl;
        this.requestMethod = requestMethod;
        this.requestData = requestData;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            URL url = new URL(apiUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestMethod);

            if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(requestData.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    response = stringBuilder.toString();
                }
            } else {
                Log.e("ApiRequestTask", "HTTP Error Code: " + statusCode);
            }
        } catch (Exception e) {
            Log.e("ApiRequestTask", "Error", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        if (listener != null) {
            listener.onApiResponse(response);
        }
    }
}
