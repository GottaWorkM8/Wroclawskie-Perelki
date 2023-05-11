package wro.per.others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImgurUploader extends AsyncTask<Void, Void, String> {

    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
    private static final String IMGUR_CLIENT_ID = "62d9881b6568e31";

    private Bitmap imageBitmap;
    private OnImageUploadListener listener;

    public ImgurUploader(Bitmap imageBitmap, OnImageUploadListener listener) {
        this.imageBitmap = imageBitmap;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection connection = null;
        DataOutputStream dataOutputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            URL url = new URL(IMGUR_UPLOAD_URL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Client-ID " + IMGUR_CLIENT_ID);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();

            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("image=" + Base64.encodeToString(imageData, Base64.DEFAULT));
            dataOutputStream.flush();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                String response = byteArrayOutputStream.toString();
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("ImgurUpload", "Exception: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject responseJson = new JSONObject(result);
                if (responseJson.getBoolean("success")) {
                    JSONArray dataArray = responseJson.getJSONArray("data");
                    JSONObject imageObject = dataArray.getJSONObject(0);
                    String imageUrl = imageObject.getString("link");
                    listener.onImageUploaded(imageUrl);
                } else {
                    listener.onImageUploadFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onImageUploadFailed();
            }
        } else {
            listener.onImageUploadFailed();
        }
    }

    public interface OnImageUploadListener {
        void onImageUploaded(String imageUrl);
        void onImageUploadFailed();
    }
}
