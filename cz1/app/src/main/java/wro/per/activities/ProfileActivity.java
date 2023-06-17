package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import wro.per.R;
import wro.per.others.JsonListReceiver;
import wro.per.others.Places;

public class ProfileActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener {

    TextView loginTextView, activityDateTextView, pointsTextView, objectsTextView, riddlesTextView, easyTextView, mediumTextView, hardTextView;
    Button logoutButton;

    ImageButton solvedButton, homeButton, favouritesButton, infoButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userKey;

    List<JSONObject> allRiddlesList = new ArrayList<>();
    List<Integer> allSolvedIDsList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> allRiddlesAndObjectsAmount = new ArrayList<>();

    Boolean getAllRiddlesBool = true;
    int howManyFoundObjects = 0;
    int howManySolvedRiddles = 0;
    int howManyPoints;
    int checkId = 0;

    @Override
    public void onJsonReceived(List<JSONObject> jsonObjects) {
        if (getAllRiddlesBool) {
            if (jsonObjects == null) {
                Toast.makeText(this, "Brak zagadek", Toast.LENGTH_SHORT).show();
            } else {
                getAllRiddlesBool = false;
                allRiddlesList.addAll(jsonObjects);
                getIdsAndAmount();
            }
        } else {
            if (jsonObjects == null) {
                Toast.makeText(this, "Błąd pobierania danych", Toast.LENGTH_SHORT).show();
            } else {
                allRiddlesAndObjectsAmount.get(checkId).add(jsonObjects.size());
                howManyFoundObjects+=jsonObjects.size();
//                System.out.println("Zostało znalezionych " + jsonObjects.size() + " z " + allRiddlesAndObjectsAmount.get(checkId).get(1) + " obiektów");
//                allSolvedIDsList.add(allRiddlesAndObjectsAmount.get(checkId).get(0));
//                System.out.println("rozwiązane zagadki: " + allSolvedIDsList);
            }
            checkId++;
            if (checkId < allRiddlesAndObjectsAmount.size()) {
                findAllSolved(checkId);
            } else {
                countSolved();
            }
        }
    }

    private void countSolved() {
        for(ArrayList<Integer> riddle : allRiddlesAndObjectsAmount)
        {
//            System.out.println("Zagadka: "+riddle);
            if(Objects.equals(riddle.get(1), riddle.get(3)) && riddle.get(1)!=0)
            {
                howManySolvedRiddles++;
//                System.out.println("Dodano rozwiązaną zagadkę");
            }

            howManyPoints+= riddle.get(2)*riddle.get(3);
//            System.out.println("Dodano "+riddle.get(2)*riddle.get(3)+" punktów");
        }
        showAllData();
    }

    private void showAllData() {
        objectsTextView.setText(String.valueOf(howManyFoundObjects));
        riddlesTextView.setText(String.valueOf(howManySolvedRiddles));
        pointsTextView.setText(String.valueOf(howManyPoints));

    }

    private void getIdsAndAmount() {
        for (int i = 0; i < allRiddlesList.size(); i++) {
            JSONObject riddle = allRiddlesList.get(i);
            try {
                int riddleId = riddle.getInt("id");
                int objectCount = riddle.getInt("objectCount");
                int points = riddle.getInt("points");
                allRiddlesAndObjectsAmount.add(new ArrayList<Integer>() {{
                    add(riddleId);
                    add(objectCount);
                    add(points);
                }});
            } catch (JSONException ignored) {

            }
        }
        if (allRiddlesAndObjectsAmount.size() > 0)
            findAllSolved(checkId);
    }

    private void findAllSolved(int riddleListIndex) {
        int riddleID = allRiddlesAndObjectsAmount.get(riddleListIndex).get(0);
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka/" + riddleID + "/znalezioneObiekty?key=" + userKey;
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userKey = sharedPreferences.getString("userKey", "defaultKey");

        solvedButton = findViewById(R.id.listButton);
        solvedButton.setOnClickListener(view -> openSolvedActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        setElementsIds();
        setLogin();
        Places places = Places.getInstance();

        getAllRiddlesFromApi();
//        System.out.println(places.getPlaces());
    }

    private void getAllRiddlesFromApi() {
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    private void setLogin() {
        String login = sharedPreferences.getString("userLogin", "Login");
        loginTextView.setText(login);
    }

    private void setElementsIds() {
        loginTextView = findViewById(R.id.loginTextView);
        pointsTextView = findViewById(R.id.points_get_textView);
        objectsTextView = findViewById(R.id.objects_found_textView);
        riddlesTextView = findViewById(R.id.riddles_solved_textView);
        easyTextView = findViewById(R.id.easy_solved_textview);
        mediumTextView = findViewById(R.id.medium_solved_textview);
        hardTextView = findViewById(R.id.hard_solved_textview);
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> logOut());
    }

    private void logOut() {
        editor.putString("userKey","");
        editor.putString("userLogin", "");
        editor.putString("userPass", "");
        editor.putBoolean("logout", true);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainPageActivity() {
        finish();
    }

    public void openFavouritesActivity() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
        finish();
    }

    public void openInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
        finish();
    }
}