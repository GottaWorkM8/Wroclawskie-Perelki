package wro.per.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;

public class ObjectInfoActivity extends AppCompatActivity {

    WebView infoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_info_layout);

        Intent intent = getIntent();
        String URL = intent.getStringExtra("infoURL");

        infoWebView = findViewById(R.id.infoWebView);
        infoWebView.getSettings().setJavaScriptEnabled(true); // Opcjonalnie, jeśli potrzebujesz obsługi JavaScript na stronie

        infoWebView.loadUrl(URL);
    }
}
