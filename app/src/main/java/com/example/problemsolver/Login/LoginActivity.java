package com.example.problemsolver.Login;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginBtn;
    private String email, password;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.login_input_email);
        passwordField = findViewById(R.id.login_input_password);
        loginBtn = findViewById(R.id.btn_login);
        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);

        loginBtn.setOnClickListener(view -> {
            email = emailField.getText().toString();
            password = passwordField.getText().toString();
            LoginForm loginForm = new LoginForm(email, password);

            ApplicationService.getInstance()
                    .getJSONApi()
                    .login(loginForm)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            SharedPreferences.Editor prefEditor = settings.edit();
                            prefEditor.putString("JWT", response.headers().get("Authorization"));
                            prefEditor.apply();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });
        });

    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

}
