package com.example.problemsolver.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Authorized.AuthorizedPerson;
import com.example.problemsolver.Authorized.PersonArea;
import com.example.problemsolver.Event.EventActivity;
import com.example.problemsolver.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView FScView, emailView, numberView, dateView, area1View, area2View, area3View, profileStatus;
    private Button myInfo, myProblems, eventsBtn;
    private AuthorizedPerson authorizedPerson;
    private String FSc, email, number, date, area1, area2, area3, role;
    private TextView [] areaView = new TextView[3];

    List<PersonArea> personAreas;

    private String token;
    private String id;
    private SharedPreferences settings;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        id = settings.getString("id","");

        FScView = findViewById(R.id.profile_name);
        emailView = findViewById(R.id.profile_email);
        numberView = findViewById(R.id.profile_number);
        dateView = findViewById(R.id.profile_date);
        area1View = findViewById(R.id.profile_area1);
        area2View = findViewById(R.id.profile_area2);
        area3View = findViewById(R.id.profile_area3);
        profileStatus = findViewById(R.id.profile_status);
        eventsBtn = findViewById(R.id.btn_events);
        eventsBtn.setVisibility(View.INVISIBLE);

        eventsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EventActivity.class);
            startActivity(intent);
        });


        ApplicationService.getInstance()
                .getJSONApi()
                .getAuthorizedPersonInfo(token, id)
                .enqueue(new Callback<AuthorizedPerson>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthorizedPerson> call, @NonNull Response<AuthorizedPerson> response) {
                        if (response.isSuccessful()) {
                            role = settings.getString("Roles", "");
                            if(role.equals("ROLE_ADMIN")) {
                                eventsBtn.setVisibility(View.VISIBLE);
                                profileStatus.setText("Администратор");
                            }
                            else if(role.equals("ROLE_USER")) {
                                eventsBtn.setVisibility(View.GONE);
                                profileStatus.setText("Пользователь");
                            }

                            authorizedPerson = response.body();
                            FSc = authorizedPerson.getFirstName() + " " + authorizedPerson.getSecondName();
                            email = authorizedPerson.getEmail();
                            number = authorizedPerson.getPhone();
                            date = authorizedPerson.getBirthDate();
                            personAreas = authorizedPerson.getPersonAreas();


                            FScView.setText(FSc);
                            emailView.setText(email);
                            numberView.setText(number);
                            dateView.setText(date);


                            areaView[0] = area1View;
                            areaView[1] = area2View;
                            areaView[2] = area3View;

                            int i = 0;
                            for (PersonArea personArea: personAreas){
                                areaView[i].setText(personArea.getAreaName());
                                i++;
                            }
                            //showMessage("Получили инфу о person для лк");
                        } else {
                            showMessage("person в лк не получен, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AuthorizedPerson> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: person в лк");
                    }
                });
    }
    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
