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
import java.util.HashMap;

import wro.per.R;
import wro.per.fragments.UnsolvedFragment;
import wro.per.fragments.SolvedFragment;
import wro.per.fragments.InProgressFragment;
import wro.per.others.TabAdapter;
import wro.per.others.Riddles;

public class SolvedActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


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