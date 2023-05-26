package wro.per.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import wro.per.R;
import wro.per.others.SendJsonTask;

public class LoginActivity extends AppCompatActivity implements SendJsonTask.ResponseListener {

    private EditText loginEditText, passwordEditText;
    private TextView errorTextView;
    private Button loginButton;

    Boolean result = false;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //metoda wykonywana po odebraniu odpowiedzi od api
    // jeżeli git to zapisuje login oraz hasło i przechodzi dalej (kalibracja)
    // albo wyświetla komunikat błędu
    @Override
    public void onResponseReceived(String response) {
        System.out.println("Odpowiedz: " + response);

        if (response.equals("true")) {
            String login, password;
            login = loginEditText.getText().toString();
            password = passwordEditText.getText().toString();

            System.out.println(login);
            System.out.println(password);

            editor.putString("userLogin", login);
            editor.putString("userPass", password);
            editor.apply();

            next();
        } else {
            errorTextView.setText("Błędny login lub hasło");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Boolean czyZalogowano = sharedPreferences.getBoolean("zalogowano", false);
            if(czyZalogowano)
                next();

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> logIn());

        loginEditText = findViewById(R.id.login_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        errorTextView = findViewById(R.id.error_textView);

        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationPage();
            }
        });

        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Wywołaj swoją akcję tutaj po wpisaniu znaku w EditText
                errorTextView.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Wywołaj swoją akcję tutaj po wpisaniu znaku w EditText
                errorTextView.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    logIn();
                    return true;
                }
                return false;
            }
        });
    }

    private void openRegistrationPage() {
        String url = "https://szajsjem.mooo.com/register.html";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    public void logIn() {
        String login, password;
        login = loginEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            errorTextView.setText("Nie wpisano wszystkich danych");
            return;
        }

        String apiUrl = "https://szajsjem.mooo.com/api/user/testlogin";
        String jsonData = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";

        SendJsonTask sendJsonTask = new SendJsonTask(this);

        sendJsonTask.execute(apiUrl, jsonData);

    }

    public void next() {
        editor.putBoolean("zalogowano", true);
        editor.apply();
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
        finish();
    }
}