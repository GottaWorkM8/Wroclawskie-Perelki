package wro.per.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import wro.per.R;

public class WTrakcieZagadkiActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_w_trakcie_zagadek_layout);

        final ImageButton otworzStroneGlownaButton;
        final Button unfinishedButton;
        final Button finishedButton;

        otworzStroneGlownaButton = (ImageButton) findViewById(R.id.homeButton);
        otworzStroneGlownaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                openActivityHome();
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

        finishedButton = (Button) findViewById(R.id.doneButton);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                openActivityFi();
            }
        });

    }

    public void openActivityHome() {
        Intent intent = new Intent(this, StronaGlownaActivity.class);
        startActivity(intent);
    }

    public void openActivityUn() {
        Intent intent = new Intent(this, NierozwiazaneZagadkiActivity.class);
        startActivity(intent);
    }

    public void openActivityFi(){
        Intent intent = new Intent(this, RozwiazaneZagadkiActivity.class);
        startActivity(intent);
    }
}