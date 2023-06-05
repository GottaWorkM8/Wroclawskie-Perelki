package wro.per.activities;

import static java.lang.Math.abs;


import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wro.per.R;
import wro.per.others.APICallAsyncTask;
import wro.per.others.ApiRequestTask;
import wro.per.others.JsonListReceiver;
import wro.per.others.JsonObjectReceiver;
import wro.per.others.JsonPostRequest;
import wro.per.others.LocationService;

public class EditObjectActivity extends AppCompatActivity implements APICallAsyncTask.OnDataReceivedListener, JsonListReceiver.JsonReceiverListener, JsonObjectReceiver.JsonReceiverListener, ApiRequestTask.ApiResponseListener {

    private Float driftX, driftZ, driftY;
    private Float accelerometerZ;
    private Float rotationZ=0f;
    float tiltInDegrees=0;
    Bitmap bitmap;
    private EditText tileEditText, detailCoordsEditText, azimuthEditText, coordinates1EditTest, coordinates2EditTest;
    float azimuth, pitch, roll;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Spinner spinnerRiddles, spinnerObjects;

    private String apiResponse;

    boolean getRiddlesFromApi = true;

    List<JSONObject> riddlesJsonList = null;
    List<JSONObject> objectsJsonList = null;

    HashMap<String, Integer> riddlesHashMap = null;
    HashMap<String, Integer> objectsHashMap = null;

    int chosenRiddleId;
    int chosenObjectId;

    List<String> keys = null;

    JSONObject objectData = null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String userKey;


    double objectLatitude=0, objectLongitude=0, observationLatitude=0, observationLongitude=0, latitude, longitude, detailLatitude=0, detailLongitude=0;

    @Override
    public void onDataReceived(String data) {
        // Obsłuż otrzymane dane
    }

    @Override
    public void onApiResponse(String data) {

    }

    private void makeAPICall(String url, String requestMethod, String dataToSend) {
//        url = "https://szajsjem.mooo.com/api/user/testlogin";
//        dataToSend = "{\"login\":\"user1\",\"password\":\"pass1\"}";
        JsonPostRequest jsonPostRequest = new JsonPostRequest(dataToSend, url);
        jsonPostRequest.execute();

    }

    @Override
    public void onJsonReceived(JSONObject jsonObject) {
        if (jsonObject != null) {
            objectData = jsonObject;
            try {
                if(objectLatitude!=0 && objectLongitude!=0) {
                    System.out.println("zmiana objectPosition");
                    objectData.put("objectPosition", objectLatitude + "," + objectLongitude);
                }
                if(observationLatitude!=0 && observationLongitude!=0) {
                    System.out.println("zmiana trackingPosition");
                    objectData.put("trackingPosition", observationLatitude + "," + observationLongitude);
                }
                if(detailLatitude!=0 && detailLongitude!=0) {
                    System.out.println("zmiana photoPosition");
                    objectData.put("photoPosition", detailLatitude + "," + detailLongitude);
                }
                if(rotationZ!=0f && tiltInDegrees!=0) {
                    System.out.println("zmiana telephoneOrientation");
                    objectData.put("telephoneOrientation", rotationZ + "," + tiltInDegrees);
                }
                String url = "https://szajsjem.mooo.com/api/miejsca/"+chosenObjectId+"?key="+userKey;
                System.out.println(url);
                System.out.println(objectData.toString());
                makeAPICall(url, "POST", objectData.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Błąd w pobieraniu danych JSON lub brak danych
        }
    }

    @Override
    public void onJsonReceived(List<JSONObject> jsonObjects) {
        if (jsonObjects != null) {
            if (getRiddlesFromApi) {
                riddlesJsonList = jsonObjects;
                setSpinner1();

            } else {
                objectsJsonList = jsonObjects;

                objectsHashMap = new HashMap<>();
                try {
                    if (objectsJsonList == null) {
                        Toast.makeText(this, "Nie udało się pobrać obiektów", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < objectsJsonList.size(); i++) {
                        JSONObject object = objectsJsonList.get(i);
                        int id = object.getInt("id");
                        String name = object.getString("objectName");

                        objectsHashMap.put(name, id);

                        // Pobierz klucze z hashmapy
                        List<String> keys = new ArrayList<>(objectsHashMap.keySet());
                        // Utwórz adapter ArrayAdapter i przekaż listę kluczy
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, keys);
                        // Określ wygląd dla rozwijanej listy
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Przypisz adapter do spinnera
                        spinnerObjects.setAdapter(adapter);

                        spinnerObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedKey = keys.get(position);
                                chosenObjectId = objectsHashMap.get(selectedKey);
                                pokaTosta2();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Nic nie rób
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "Nie udało się pobrać danych", Toast.LENGTH_SHORT).show();
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            coordinates1EditTest = findViewById(R.id.wspolrzedne_obserwacji_edittext);
            coordinates2EditTest = findViewById(R.id.wspolrzedne_obiektu_edittext);
            latitude = intent.getDoubleExtra("latitude", 3);
            longitude = intent.getDoubleExtra("longitude", 3);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_edit_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userKey = sharedPreferences.getString("userKey", "defaultKey");

        Button takePhotoButton;
        Button sendToDatabaseButton;
        Button getCoordinates1Button;
        Button getCoordinates2Button;

        tileEditText = findViewById(R.id.nachylenieEditText);
        azimuthEditText = findViewById(R.id.kierunekEditText);
        detailCoordsEditText = findViewById(R.id.wspolrzedne_szczegołu_edittext);
        getCoordinates1Button = findViewById(R.id.wspolrzedne_obserwacji_button);
        getCoordinates2Button = findViewById(R.id.wspolrzedne_obiektu_button);
        takePhotoButton = findViewById(R.id.zdjecie_szczegolu_button);
        sendToDatabaseButton = findViewById(R.id.wyslij_button);
        spinnerRiddles = findViewById(R.id.zagadki_spinner);
        spinnerObjects = findViewById(R.id.obiekty_spinner);

        getRiddlesFromApi();


        takePhotoButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });

        getCoordinates1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditObjectActivity.this, LocationService.class);
                startService(intent);
                objectLatitude = latitude;
                objectLongitude = longitude;
                coordinates1EditTest.setText(objectLatitude + ",  " + objectLongitude);
            }
        });

        getCoordinates2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditObjectActivity.this, LocationService.class);
                startService(intent);
                observationLatitude = latitude;
                observationLongitude = longitude;
                coordinates2EditTest.setText(observationLatitude + ",  " + observationLongitude);
            }
        });

        sendToDatabaseButton.setOnClickListener(view -> {

            JsonObjectReceiver jsonReceiver = new JsonObjectReceiver(this);
            jsonReceiver.execute("https://szajsjem.mooo.com/api/miejsca/"+chosenObjectId);

            Toast.makeText(this, "Wysłane", Toast.LENGTH_SHORT).show();
            System.out.println("\n\nWybrane i wpisane dane:");
            System.out.println("Zagadka: "+chosenRiddleId);
            System.out.println("Obiekt: "+chosenObjectId);
            System.out.println("Współrzędne obiektu: "+objectLatitude+", "+objectLongitude);
            System.out.println("Współrzędne obserwacji: "+observationLatitude+", "+observationLongitude);
            System.out.println("Współrzędne szczegółu: "+detailLatitude+", "+detailLongitude);
            System.out.println("Kierunek telefonu: "+rotationZ);
            System.out.println("Nachylenie telefonu: "+tiltInDegrees);

        });
    }


    private void getRiddlesFromApi() {
        getRiddlesFromApi = true;
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }


    private void getObjectsFromApi(int id) {
        getRiddlesFromApi = false;
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka/" + id + "/getMiejsca";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }


    private void setSpinner1() {
        riddlesHashMap = new HashMap<>();
        try {
            if (riddlesJsonList == null) {
                Toast.makeText(this, "Nie udało się załadować zagadek", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < riddlesJsonList.size(); i++) {
                JSONObject object = riddlesJsonList.get(i);
                int id = object.getInt("id");
                String name = object.getString("name");

                riddlesHashMap.put(name, id);
            }
            // Pobierz klucze z hashmapy
            keys = new ArrayList<>(riddlesHashMap.keySet());
            // Utwórz adapter ArrayAdapter i przekaż listę kluczy
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, keys);
            // Określ wygląd dla rozwijanej listy
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Przypisz adapter do spinnera
            spinnerRiddles.setAdapter(adapter);

            spinnerRiddles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedKey = keys.get(position);
                    chosenRiddleId = riddlesHashMap.get(selectedKey);
                    pokaTosta();
                    getObjectsFromApi(riddlesHashMap.get(selectedKey));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Nic nie rób
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void pokaTosta() {
        Toast.makeText(this, "wybrana zagadka id: " + chosenRiddleId, Toast.LENGTH_SHORT).show();
    }

    public void pokaTosta2() {
        Toast.makeText(this, "wybrany obiekt id: " + chosenObjectId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String imagePath = data.getStringExtra("imagePath");
            azimuth = data.getFloatExtra("azimuth", 0);
            pitch = data.getFloatExtra("pitch", 0);
            roll = data.getFloatExtra("roll", 0);
            accelerometerZ = data.getFloatExtra("accelerometerZ", 0);
            rotationZ = data.getFloatExtra("rotationZ", 0);
            tiltInDegrees = (float) Math.toDegrees(Math.acos(accelerometerZ / SensorManager.GRAVITY_EARTH));

            tileEditText.setText(Float.toString(tiltInDegrees));
            azimuthEditText.setText(Float.toString(rotationZ));

            detailLatitude = data.getDoubleExtra("lat", 1);
            detailLongitude = data.getDoubleExtra("lon", 1);

            detailCoordsEditText.setText(Double.toString(detailLatitude) + ",  " + Double.toString(detailLongitude));

            bitmap = BitmapFactory.decodeFile(imagePath);
            try {
                ExifInterface exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Matrix matrix = new Matrix();
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    matrix.postRotate(90);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    matrix.postRotate(180);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = findViewById(R.id.zdjecie_szczegolu_imageview);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void stopLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("ACT_LOC"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
        unregisterReceiver(broadcastReceiver);
    }
}
