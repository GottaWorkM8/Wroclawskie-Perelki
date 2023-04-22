package wro.per.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import wro.per.R;

public class RozwiazaneZagadkiActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rozwiazanych_zagadek_layout);

        final ImageButton otworzStroneGlownaButton;
        final Button unfinishedButton;
        final Button wTrakcieButton;

        otworzStroneGlownaButton = (ImageButton) findViewById(R.id.homeButton);
        otworzStroneGlownaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
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

        unfinishedButton = (Button) findViewById(R.id.unfinishedButton);
        unfinishedButton.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(this, NierozwiazaneZagadkiActivity.class);
        startActivity(intent);
        finish();
    }
    public void openActivityWTrakcie(){
        Intent intent = new Intent(this, WTrakcieZagadkiActivity.class);
        startActivity(intent);
        finish();
    }
}