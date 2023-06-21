package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import wro.per.R;

public class FoundObjectActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button buttonFind;
    private boolean found = false;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_object_layout);
        buttonFind = findViewById(R.id.found_button);
        buttonFind.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra("search",  id);
            startActivity(intent);
            finish();
        });
        imageView = findViewById(R.id.foundTextureView);
        Intent intent = getIntent();

        if (intent.hasExtra("nearbyPlace")) {
            String jsonString = intent.getStringExtra("nearbyPlace");
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                String imageUrl = jsonObject.getString("photoObjectLink");
                id = jsonObject.getInt("id");

                Picasso.get().load(imageUrl).into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}