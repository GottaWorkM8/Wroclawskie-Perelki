package com.example.cz1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainPageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page_unfinished);


        final ImageButton button;
        final Button finishedButton;

        button = (ImageButton) findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("działa");
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
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
    public void openActivityUn(){
        Intent intent = new Intent(this, strona_zagadek.class);
        startActivity(intent);
    }
}