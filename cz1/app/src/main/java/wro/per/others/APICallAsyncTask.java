package wro.per.others;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICallAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = APICallAsyncTask.class.getSimpleName();

    private Activity mActivity;
    private String mUrl;
    private String mRequestMethod;
    private String mDataToSend;
    private OnDataReceivedListener mListener;

    public APICallAsyncTask(Activity activity, String url, String requestMethod, String dataToSend, OnDataReceivedListener listener) {
        mActivity = activity;
        mUrl = url;
        mRequestMethod = requestMethod;
        mDataToSend = dataToSend;
        mListener = listener;
    }

    public interface OnDataReceivedListener {
        void onDataReceived(String data);
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(mUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(mRequestMethod);

            if (mRequestMethod.equals("POST") || mRequestMethod.equals("PUT")) {
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(mDataToSend.getBytes());
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                Log.e(TAG, "HTTP Error Code: " + statusCode);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing stream: " + e.getMessage());
                }
            }
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (mListener != null) {
            mListener.onDataReceived(result);
        }
    }
}