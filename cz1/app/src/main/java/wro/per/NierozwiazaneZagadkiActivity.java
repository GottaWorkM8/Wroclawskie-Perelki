package wro.per;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NierozwiazaneZagadkiActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_nierozwiazanych_zagadek);


        final ImageButton button;
        final Button finishedButton;

        button = (ImageButton) findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
                System.out.println();
                openActivityHome();
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
        Intent intent = new Intent(this, Strona_glowna_Activity.class);
        startActivity(intent);
    }
    public void openActivityUn(){
        Intent intent = new Intent(this, strona_zagadek.class);
        startActivity(intent);
    }
}