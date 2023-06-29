package wro.per.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.JsonListReceiver;

public class InProgressActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener {

    ImageButton profilButton, homeButton, favouritesButton, infoButton;
    Button solved, notStarted;
    LinearLayout solvedList;
    SharedPreferences sharedPreferences;
    String userKey;
    TextView errorTextView;

    ArrayList<ArrayList<Integer>> riddlesAndObjectsAmount = new ArrayList<>();
    List<JSONObject> allRiddlesList = new ArrayList<>();
    List<Integer> allInProgressIDsList = new ArrayList<>();
    Boolean getAllRiddlesBool = true;
    int checkId = 0;
    private ArrayList<String> firstObject = new ArrayList<>();

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
                if (jsonObjects.size() < riddlesAndObjectsAmount.get(checkId).get(1)) {
                    riddlesAndObjectsAmount.get(checkId).add(jsonObjects.size());
                    System.out.println("Zostało znalezionych " + jsonObjects.size() + " z " + riddlesAndObjectsAmount.get(checkId).get(1) + " obiektów");
                    allInProgressIDsList.add(riddlesAndObjectsAmount.get(checkId).get(0));
                    System.out.println("rozwiązane zagadki: " + allInProgressIDsList);
                    try {
                        firstObject.add(jsonObjects.get(0).getString("photoLink"));
                    } catch (JSONException e) {
                        firstObject.add("null");
                    }
                }
            }
            checkId++;
            if (checkId < riddlesAndObjectsAmount.size()) {
                findAllSolved(checkId);
            } else {
                showAllSolved();
            }
        }
    }


    private void showAllSolved() {
        if(allInProgressIDsList.size()==0) {
            errorTextView.setText("Brak zagadek w trakcie");
            return;
        }
        errorTextView.setVisibility(View.GONE);
        for (int i = 0; i < allInProgressIDsList.size(); i++) {
            JSONObject riddle = findRiddleById(allInProgressIDsList.get(i));
            int riddleId;
            String riddleName;
            int objectCount;
            String difficulty;
            String photoLink;
            String author;
            try {
                riddleId = riddle.getInt("id");
                riddleName = riddle.getString("name");
                objectCount = riddle.getInt("objectCount");
                difficulty = riddle.getString("difficulty");
                photoLink = firstObject.get(i);
                author = riddle.getString("author");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            View tile = getLayoutInflater().inflate(R.layout.in_progress_tile, null, false);
            TextView name = tile.findViewById(R.id.name);
            name.setText(riddleName);
            TextView objectCountText = tile.findViewById(R.id.objectsCount);
            objectCountText.setText(String.valueOf(objectCount));
            int foundCount = 0;
            for (int j = 0; j < riddlesAndObjectsAmount.size(); j++) {
                if (riddlesAndObjectsAmount.get(j).get(0) == riddleId)
                    foundCount = riddlesAndObjectsAmount.get(j).get(2);
            }
            TextView foundCountText = tile.findViewById(R.id.FoundCount);
            foundCountText.setText(String.valueOf(foundCount));
            TextView difficultyText = tile.findViewById(R.id.difficulty);
            difficultyText.setText(difficulty);
            TextView authorText = tile.findViewById(R.id.author);
            authorText.setText(author);
            TextView backgroundImage = tile.findViewById(R.id.info_text);
            System.out.println(photoLink);
            Picasso.get()
                    .load(photoLink)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            backgroundImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            // Obsługa błędu ładowania obrazka
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            // Przygotowanie do ładowania obrazka
                        }
                    });

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
                riddlesAndObjectsAmount.add(new ArrayList<Integer>() {{
                    add(riddleId);
                    add(objectCount);
                }});
            } catch (JSONException ignored) {

            }
        }
        if (riddlesAndObjectsAmount.size() > 0)
            findAllSolved(checkId);
    }

    private void findAllSolved(int riddleListIndex) {
        int riddleID = riddlesAndObjectsAmount.get(riddleListIndex).get(0);
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
        setContentView(R.layout.in_progress_layout);

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

        solved = findViewById(R.id.rozwiazaneButton);
        solved.setOnClickListener(view -> openSolvedActivity());

        notStarted = findViewById(R.id.nierozpoczeteButton);
        notStarted.setOnClickListener(view -> openNotStartedActivity());

        solvedList = findViewById(R.id.inProgressScrollList);

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

    private void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivity.class);
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