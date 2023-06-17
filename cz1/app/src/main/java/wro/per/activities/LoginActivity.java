package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import wro.per.R;
import wro.per.others.ApiRequestTask;

public class LoginActivity extends AppCompatActivity implements ApiRequestTask.ApiResponseListener {

    private EditText loginEditText, passwordEditText;
    private TextView errorTextView;
    private Button loginButton;

    Boolean result = false;

    private String login, password;
    private String tempLogin, tempPassword;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Boolean keyOK = false;

    Boolean testLoginBool = false, loginBool = false, testKeyBool = false;
    boolean testIfAlreadyLoginBool = false;

    @Override
    public void onApiResponse(String data) {
        //sprawdzanie czy aktualny klucz jest aktualny
        if (testIfAlreadyLoginBool) {
            testIfAlreadyLoginBool = false;
            if (data.equals("true")) {
                System.out.println(sharedPreferences.getString("userLogin", "pusto"));

                next();
            }
        }
        //sprawdzanie wpisanych danych
        else if (testLoginBool) {
            if (data.equals("true")) {
                testLoginBool = false;

                editor.putString("userLogin", login);
                editor.putString("userPass", password);
                editor.apply();

                loginBool = true;
                makeAPICall("https://szajsjem.mooo.com/api/user/login", "POST", "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}");

            } else if (data.equals("false")) {
                testLoginBool = false;
                errorTextView.setText("Błędny login lub hasło");
            }
        } else if (loginBool) {
            if (data.equals("false")) {
                System.out.println("Klucz się nie wygenerował");
            } else {
                System.out.println("Generated key: " + data);
                editor.putString("userKey", data);
                editor.apply();
            }

            loginBool = false;
            next();
        } else if (testKeyBool) {
            if (data.equals("true")) {
                keyOK = true;
                testKeyBool = false;

                next();
            } else {
                keyOK = false;
                testKeyBool = false;

            }
        }
    }

    private void makeAPICall(String url, String requestMethod, String dataToSend) {
        ApiRequestTask apiRequestTask = new ApiRequestTask(url, requestMethod, dataToSend, this);
        apiRequestTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent = getIntent();
        Boolean logout = intent.getBooleanExtra("logout", false);
        if (!logout) {
            String key = sharedPreferences.getString("userKey", "");
            String login = sharedPreferences.getString("userLogin", "");
            String pass = sharedPreferences.getString("userPass", "");
            if (!key.equals("") && !login.equals("") && !pass.equals("")) {
                testIfAlreadyLoginBool = true;
                String url = "https://szajsjem.mooo.com/api/user/testkey?key=" + key;
                makeAPICall(url, "GET", "");
            }
        }


//        Boolean czyZalogowano = sharedPreferences.getBoolean("zalogowano", false);
//        if(czyZalogowano)
//        {
//            String key = sharedPreferences.getString("userKey", "brak");
//            testKeyBool=true;
//            String url = "https://szajsjem.mooo.com/api/user/testkey?key="+key;
//            makeAPICall(url, "GET", "");
//
//
//
////            loginBool=true;
////            makeAPICall("https://szajsjem.mooo.com/api/user/login", "POST", "{\"login\":\"user1\",\"password\":\"pass1\"}");
//
//        }
//        else {
//
//        }

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> checkPass());

        loginEditText = findViewById(R.id.login_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        errorTextView = findViewById(R.id.error_textView);

        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> openRegistrationPage());

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

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPass();
                return true;
            }
            return false;
        });
    }

    private void openRegistrationPage() {
        String url = "https://szajsjem.mooo.com/register.html";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void checkPass() {
        login = loginEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            errorTextView.setText("Nie wpisano wszystkich danych");
            return;
        }
        testLoginBool = true;
        System.out.println("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}");
        makeAPICall("https://szajsjem.mooo.com/api/user/testlogin", "POST", "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}");

    }

    public void next() {
        editor.putBoolean("zalogowano", true);
        editor.apply();
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
        finish();
    }
}