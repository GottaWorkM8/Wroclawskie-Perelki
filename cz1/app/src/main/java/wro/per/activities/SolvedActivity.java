package wro.per.activities;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;

import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import wro.per.R;
import wro.per.fragments.UnsolvedFragment;
import wro.per.fragments.SolvedFragment;
import wro.per.fragments.InProgressFragment;
import wro.per.others.TabAdapter;
import wro.per.others.Riddles;

public class SolvedActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ArrayList<Riddles> riddlesArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        class FetchData extends Thread {

            public ArrayList<Riddles> riddlesArrList = new ArrayList<>();

            @Override
            public void run() {
                try {
                    URL url = new URL("https://szajsjem.mooo.com/api/zagadka");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    StringBuilder data = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        data.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(data.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Riddles riddle = new Riddles();
                        riddle.setId(jsonObject.getInt("id"));
                        riddle.setDifficulty(jsonObject.getString("difficulty"));
                        riddle.setName(jsonObject.getString("name"));
                        riddle.setObjectCount(jsonObject.isNull("objectCount") ? null : jsonObject.getInt("objectCount"));
                        riddle.setInfoLink(jsonObject.getString("infolink"));
                        riddle.setAuthor(jsonObject.getString("author"));
                        riddle.setPoints(jsonObject.isNull("points") ? null : jsonObject.getInt("points"));
                        riddlesArrList.add(riddle);
                    }
                    bufferedReader.close();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        FetchData fetchData = new FetchData();
        fetchData.start(); // uruchamia wątek i wywołuje metodę run()

        while (fetchData.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        riddlesArrayList = fetchData.riddlesArrList;

        setContentView(R.layout.solved_layout);

        final ImageButton openMainPageImageButton;

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        tabAdapter.addFragment(new SolvedFragment(), "Rozwiązane");
        tabAdapter.addFragment(new InProgressFragment(), "W trakcie");
        tabAdapter.addFragment(new UnsolvedFragment(), "Nierozwiązane");

        viewPager.setAdapter(tabAdapter);

        openMainPageImageButton = (ImageButton) findViewById(R.id.homeButton);
        openMainPageImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });
    }

    public void openActivityHome() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}