package com.example.problemsolver.Event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.R;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPageActivity extends AppCompatActivity {


    private TextView address, date, type, description, rating;//, status;
    private Button acceptBtn, rejectBtn;
    private static final String statusCreated = "created";
    private static final String statusInProcess = "in process";
    private static final String statusSolved = "solved";
    private static final String statusRejected = "rejected";
    private SharedPreferences settings;
    private String token, personId, problemId, eventStatus, eventId, result, moderatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_problem_page);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        personId = settings.getString("id", "");
        problemId = getIntent().getStringExtra("problem_id");
        eventStatus = getIntent().getStringExtra("event_status");
        eventId = getIntent().getStringExtra("event_id");

        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        type = findViewById(R.id.problem_type);
        description = findViewById(R.id.problem_description);
        //rating = findViewById(R.id.rating);

        //status = findViewById(R.id.status);
        acceptBtn = findViewById(R.id.accept);
        rejectBtn = findViewById(R.id.reject);

        //rating.setText(getIntent().getStringExtra("problem_rating"));
        address.setText(getIntent().getStringExtra("problem_address"));


        rejectBtn.setOnClickListener(view -> {
            eventStatus = "rejected";
            ApplicationService.getInstance()
            .getJSONApi()
            .updateEvent(token, eventId,"",eventStatus, "rejected", personId)
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    showMessage("Отклонено");
                    Intent intent = new Intent(EventPageActivity.this, EventActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });

        acceptBtn.setOnClickListener(view -> {
            if(eventStatus.equals("created")) {
                eventStatus = "in process";
            }
            else if(eventStatus.equals("in process")) {
                eventStatus = "solved";
            }

            ApplicationService.getInstance()
                    .getJSONApi()
                    .updateEvent(token, eventId,"",eventStatus, "accepted", personId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            showMessage("Принято");
                            Intent intent = new Intent(EventPageActivity.this, EventActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
        });


        address.setText(getIntent().getStringExtra("problem_address"));
        type.setText(getIntent().getStringExtra("problem_type"));
        description.setText(getIntent().getStringExtra("problem_description"));
        date.setText(getIntent().getStringExtra("problem_date"));
        //rating.setText("Рейтинг: " + getIntent().getStringExtra("problem_rating"));




        String serverStatus = getIntent().getStringExtra("problem_status");
        switch (serverStatus) {
            case "created":
                serverStatus = "создана";
                break;
            case "in process":
                serverStatus = "в процессе";
                break;
            case "solved":
                serverStatus = "решена";
                break;
            case "rejected":
                serverStatus = "отклонена";
                break;
        }

        //status.setText("Статус: " + serverStatus);
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
