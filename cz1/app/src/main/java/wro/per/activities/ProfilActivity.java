package wro.per.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;
import wro.per.fragments.MenuFragment;

public class ProfilActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rozwiniete_menu_layout);

        Button showTextButton = findViewById(R.id.show_text_button);
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        showTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Utwórz instancję fragmentu z tekstem
                MenuFragment textFragment = new MenuFragment();

                // Dodaj fragment do kontenera
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(fragmentContainer.getId(), textFragment)
                        .commit();

                // Zmień rozmiar kontenera na połowę wysokości ekranu
                ViewGroup.LayoutParams params = fragmentContainer.getLayoutParams();
                params.height = fragmentContainer.getHeight() / 2;
                fragmentContainer.setLayoutParams(params);
            }
        });

    }
}
