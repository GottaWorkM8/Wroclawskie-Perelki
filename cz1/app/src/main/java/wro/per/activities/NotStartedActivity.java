package wro.per.activities;

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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.JsonListReceiver;

public class NotStartedActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener {

    ImageButton profilButton, homeButton, favouritesButton, infoButton;
    Button inProgress, solved;
    LinearLayout solvedList;
    SharedPreferences sharedPreferences;
    String userKey;

    ArrayList<ArrayList<Integer>> AllRiddlesAndObjectsAmount = new ArrayList<>();
    List<JSONObject> allRiddlesList = new ArrayList<>();
    List<Integer> allNotStartedIDsList = new ArrayList<>();
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
                for(int i=0;i<jsonObjects.size();i++)
                {
                    int objectCount;
                    try {
                        objectCount = jsonObjects.get(i).getInt("objectCount");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if(objectCount>0)
                        allRiddlesList.add(jsonObjects.get(i));
                }
                getIdsAndAmount();
            }
        } else {
            if (jsonObjects == null || jsonObjects.size() == 0) {
                System.out.println("Zostało znalezionych " + jsonObjects.size() + " z " + AllRiddlesAndObjectsAmount.get(checkId).get(1) + " obiektów");
                allNotStartedIDsList.add(AllRiddlesAndObjectsAmount.get(checkId).get(0));
                System.out.println("rozwiązane zagadki: "+ allNotStartedIDsList);

            } else {
                System.out.println("Jakieś już znaleziono");
            }
            checkId++;
            if (checkId < AllRiddlesAndObjectsAmount.size()) {
                findAllSolved(checkId);
            } else {
                showNotStarted();
            }
        }
    }
    private void showNotStarted() {
        if(allNotStartedIDsList.size()==0)
        {
            errorTextView.setText("Brak nierozpoczętych zagadek");
            return;
        }
        errorTextView.setVisibility(View.GONE);
        for (int i = 0; i < allNotStartedIDsList.size(); i++) {
            JSONObject riddle = findRiddleById(allNotStartedIDsList.get(i));
            int riddleId;
            String riddleName;
            int objectCount;
            String difficulty;
            String author;
            try {
                riddleId = riddle.getInt("id");
                riddleName = riddle.getString("name");
                objectCount = riddle.getInt("objectCount");
                difficulty = riddle.getString("difficulty");
                author = riddle.getString("author");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            View tile = getLayoutInflater().inflate(R.layout.not_started_tile, null, false);
//            TextView name = tile.findViewById(R.id.name);
//            name.setText(riddleName);
            TextView objectCountText = tile.findViewById(R.id.objectsCount);
            objectCountText.setText(String.valueOf(objectCount));
            TextView difficultyText = tile.findViewById(R.id.difficulty);
            difficultyText.setText(difficulty);
            TextView authorText = tile.findViewById(R.id.author);
            authorText.setText(author);
            tile.setOnClickListener(view -> {
                Intent intent = new Intent(this, ObjectListActivity.class);
                intent.putExtra("riddleID", riddleId);
                intent.putExtra("riddleName", riddleName);
                intent.putExtra("showRiddleName", false);
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
                AllRiddlesAndObjectsAmount.add(new ArrayList<Integer>() {{
                    add(riddleId);
                    add(objectCount);
                }});
            } catch (JSONException ignored) {

            }
        }
        if (AllRiddlesAndObjectsAmount.size() > 0)
            findAllSolved(checkId);
    }

    private void findAllSolved(int riddleListIndex) {
        int riddleID = AllRiddlesAndObjectsAmount.get(riddleListIndex).get(0);
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka/" + riddleID + "/znalezioneObiekty?key=" + userKey;
        System.out.println(apiUrl);
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(data.getBooleanExtra("close", false))
//        {
//            System.out.println("Zamykanie");
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_started_layout);



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

        solved = findViewById(R.id.rozwiazaneButton);
        solved.setOnClickListener(view -> openSolvedActivity());

        solvedList = findViewById(R.id.notStartedList);



        // pobieramy z api listę wszystkich zagadek
        String apiUrl = "https://szajsjem.mooo.com/api/zagadka";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    private void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivity.class);
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
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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