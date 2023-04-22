package wro.per.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;

public class NierozwiazaneZagadkiActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_nierozwiazanych_zagadek_layout);


        final ImageButton otworzStroneGlownaButton;
        final Button finishedButton;
        final Button wTrakcieButton;

        otworzStroneGlownaButton = (ImageButton) findViewById(R.id.homeButton);
        otworzStroneGlownaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                System.out.println();
                openActivityHome();
            }
        });

        wTrakcieButton = (Button) findViewById(R.id.wTrakcieButton);
        wTrakcieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                openActivityWTrakcie();
            }
        });

        finishedButton = (Button) findViewById(R.id.doneButton);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                openActivityUn();
            }
        });

    }

    public void openActivityHome(){
        Intent intent = new Intent(this, StronaGlownaActivity.class);
        startActivity(intent);
        finish();
    }
    public void openActivityUn(){
        Intent intent = new Intent(this, RozwiazaneZagadkiActivity.class);
        startActivity(intent);
        finish();
    }

    public void openActivityWTrakcie(){
        Intent intent = new Intent(this, WTrakcieZagadkiActivity.class);
        startActivity(intent);
        finish();
    }
}