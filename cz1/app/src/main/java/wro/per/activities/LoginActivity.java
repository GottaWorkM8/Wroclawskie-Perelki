package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import wro.per.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText, passwordEditText;
    private TextView errorTextView;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> logIn());

        loginEditText = findViewById(R.id.login_edittext);
        passwordEditText = findViewById(R.id.password_edittext);

        errorTextView = findViewById(R.id.error_textView);
    }

    public void logIn() {
        String login, password;
        login = loginEditText.getText().toString();
        password = passwordEditText.getText().toString();

        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("login", login);
            loginJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendLoginRequest(loginJson);
    }

    private void sendLoginRequest(JSONObject loginJson) {
        String url = "http://szajsjem.mooo.com/api/user/testlogin"; // Adres URL do API

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, loginJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        errorTextView.setText("Wysyłanie");
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // Zalogowano pomyślnie
                                Log.d("LoginActivity", "Zalogowano");
                                errorTextView.setText("Błąd");
                            } else {
                                // Nie udało się zalogować
                                Log.d("LoginActivity", "Nie udało się zalogować");
                                errorTextView.setText("Jest git");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Błąd sieci lub odpowiedź HTTP innego niż 2xx
                Log.d("LoginActivity", "Błąd sieci");
            }
        });

        requestQueue.add(jsonObjectRequest);
        //next();
    }

    public void next()
    {
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
        finish();
    }


}