package wro.per.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;

import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import wro.per.R;
import wro.per.fragments.FragmentNierozwiazane;
import wro.per.fragments.FragmentRozwiazane;
import wro.per.fragments.FragmentWTrakcie;
import wro.per.others.TabAdapter;

public class RozwiazaneZagadkiActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rozwiazanych_zagadek_layout);

        final ImageButton otworzStroneGlownaButton;

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        tabAdapter.addFragment(new FragmentRozwiazane(), "Rozwiązane");
        tabAdapter.addFragment(new FragmentWTrakcie(), "W trakcie");
        tabAdapter.addFragment(new FragmentNierozwiazane(), "Nierozwiązane");


        viewPager.setAdapter(tabAdapter);

        otworzStroneGlownaButton = (ImageButton) findViewById(R.id.homeButton);
        otworzStroneGlownaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                openActivityHome();
            }
        });



    }

    public void openActivityHome(){
        Intent intent = new Intent(this, StronaGlownaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}