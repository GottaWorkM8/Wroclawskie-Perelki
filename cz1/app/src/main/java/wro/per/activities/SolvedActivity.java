package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.JsonListReceiver;

public class SolvedActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener {

    ImageButton profilButton, homeButton, favouritesButton, infoButton;
    Button inProgress, notStarted;
    LinearLayout solvedList;
    SharedPreferences sharedPreferences;
    String userKey;
    List<Integer> allSolvedIDsList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> allRiddlesAndObjectsAmount = new ArrayList<>();
    List<JSONObject> allRiddlesList = new ArrayList<>();

    Boolean getAllRiddlesBool = true;
    int checkId = 0;
    TextView errorTextView;

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
            if (jsonObjects == null || jsonObjects.size() == 0) {
                System.out.println("Pusta lista lub zero znalezionych");
            } else {
                if (jsonObjects.size() == allRiddlesAndObjectsAmount.get(checkId).get(1)) {

                    System.out.println("Zostało znalezionych " + jsonObjects.size() + " z " + allRiddlesAndObjectsAmount.get(checkId).get(1) + " obiektów");
                    allSolvedIDsList.add(allRiddlesAndObjectsAmount.get(checkId).get(0));
                    System.out.println("rozwiązane zagadki: "+allSolvedIDsList);
                }
            }
            checkId++;
            if (checkId < allRiddlesAndObjectsAmount.size()) {
                findAllSolved(checkId);
            } else {
                showAllSolved();
            }
        }
    }



    private void showAllSolved() {
        if(allSolvedIDsList.size()==0) {
            errorTextView.setText("Brak rozwiązanych zagadek");
            return;
        }
            errorTextView.setVisibility(View.GONE);
        for (int i = 0; i < allSolvedIDsList.size(); i++) {
            JSONObject riddle = findRiddleById(allSolvedIDsList.get(i));
            int riddleId;
            String riddleName;
            int objectCount;
            String difficulty;
            try {
                riddleId = riddle.getInt("id");
                riddleName = riddle.getString("name");
                objectCount = riddle.getInt("objectCount");
                difficulty = riddle.getString("difficulty");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            View tile = getLayoutInflater().inflate(R.layout.solved_tile, null, false);
            TextView name = tile.findViewById(R.id.name);
            name.setText(riddleName);
            TextView objectCountText = tile.findViewById(R.id.objectsCount);
            objectCountText.setText(String.valueOf(objectCount));
            TextView difficultyText = tile.findViewById(R.id.difficulty);
            difficultyText.setText(difficulty);
            tile.setOnClickListener(view -> {
                Intent intent = new Intent(this, ObjectListActivity.class);
                intent.putExtra("riddleID", riddleId);
                intent.putExtra("riddleName", riddleName);
                startActivity(intent);
            });
            solvedList.addView(tile);
        }
    }

    private JSONObject findRiddleById(Integer id) {
        for (int i = 0; i < allRiddlesList.size(); i++) {
            int riddleID = 0;
            try {
                riddleID = allRiddlesList.get(i).getInt("id");
            } catch (JSONException ignored) {

            }
            if (riddleID == id)
                return allRiddlesList.get(i);
        }
        return null;
    }

    private void getIdsAndAmount() {
        for (int i = 0; i < allRiddlesList.size(); i++) {
            JSONObject riddle = allRiddlesList.get(i);
            try {
                int riddleId = riddle.getInt("id");
                int objectCount = riddle.getInt("objectCount");
                System.out.println("Zagadka od id " + riddleId + " ma " + objectCount + " obiektów.");
                allRiddlesAndObjectsAmount.add(new ArrayList<Integer>() {{
                    add(riddleId);
                    add(objectCount);
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
        System.out.println(apiUrl);
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solved_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        userKey = sharedPreferences.getString("userKey", "defaultKey");

        errorTextView = findViewById(R.id.errorTextView);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        inProgress = findViewById(R.id.wTrakcieButton);
        inProgress.setOnClickListener(view -> openInProgressActivity());

        notStarted = findViewById(R.id.nierozpoczeteButton);
        notStarted.setOnClickListener(view -> openNotStartedActivity());

        solvedList = findViewById(R.id.solvedList);

        // pobieramy z api listę wszystkich zagadek
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    private void openNotStartedActivity() {
        Intent intent = new Intent(this, NotStartedActivity.class);
        startActivity(intent);
        finish();
    }

    private void openInProgressActivity() {
        Intent intent = new Intent(this, InProgressActivity.class);
        startActivity(intent);
        finish();
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
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