package wro.per.activities;

import android.content.Intent;
import android.os.Bundle;


import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import wro.per.R;
import wro.per.fragments.InProgressFragment;
import wro.per.fragments.SolvedFragment;
import wro.per.fragments.UnsolvedFragment;
import wro.per.others.TabAdapter;

public class SolvedActivityOld extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    ImageButton profilButton, homeButton, favouritesButton, infoButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.solved_layout_old);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view->openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view-> openInfoActivity());

        tabLayout.setupWithViewPager(viewPager);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        tabAdapter.addFragment(new SolvedFragment(), "Rozwiązane");
        tabAdapter.addFragment(new InProgressFragment(), "W trakcie");
        tabAdapter.addFragment(new UnsolvedFragment(), "Nierozwiązane");

        viewPager.setAdapter(tabAdapter);

    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfilActivity.class);
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