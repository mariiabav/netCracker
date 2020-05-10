package com.example.problemsolver.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.DashboardActivity;
import com.example.problemsolver.personRoles.PersonRoles;
import com.example.problemsolver.R;
import com.example.problemsolver.registration.RegistrationActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginBtn, registerBtn;
    private String email, password, token, role, id;

    private static SharedPreferences settings;
    private PersonRoles personRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editText_login_email);
        passwordField = findViewById(R.id.editText_login_password);
        loginBtn = findViewById(R.id.btn_login);
        registerBtn = findViewById(R.id.btn_signup);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_MULTI_PROCESS);

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
                            token = response.headers().get("Authorization");
                            if(token != null) {
                                settings.edit().putString("JWT", token).commit();
                                ApplicationService.getInstance()
                                        .getJSONApi()
                                        .getPersonRoles(token)
                                        .enqueue(new Callback<PersonRoles>() {
                                            @Override
                                            public void onResponse(@NonNull Call<PersonRoles> call, @NonNull Response<PersonRoles> response) {
                                                if (response.isSuccessful()) {
                                                    personRoles = response.body();
                                                    role = personRoles.getRole().getName();
                                                    id = personRoles.getId();
                                                    settings.edit().putString("Roles", role).commit();
                                                    settings.edit().putString("id", id).commit();
                                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<PersonRoles> call, @NonNull Throwable t) {

                                            }
                                        });
                            }
                            else{
                                showMessage("Неверный логин или пароль");
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });


        });

        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}


