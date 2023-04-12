package wro.per;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class strona_zagadek extends Activity {




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rozwiazanych_zagadek);




        final ImageButton button;
        final Button unfinishedButton;

        button = (ImageButton) findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
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

    }

    public void openActivityHome(){
        Intent intent = new Intent(this, Strona_glowna_Activity.class);
        startActivity(intent);
    }
    public void openActivityUn(){
        Intent intent = new Intent(this, NierozwiazaneZagadkiActivity.class);
        startActivity(intent);
    }
}