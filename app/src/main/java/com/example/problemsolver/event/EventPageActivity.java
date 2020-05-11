package com.example.problemsolver.event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.R;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPageActivity extends AppCompatActivity {


    private TextView address, date, type, description, result_user_comment;
    private Button acceptBtn, rejectBtn;
    private SharedPreferences settings;
    private String token, personId, problemId, eventStatus, eventId, result, moderatorId;
    private String scale;
    private RadioGroup radioGroup;
    private ImageView mProblemImg;

    private String statusInit = "init", statusCreated = "created", statusNotified = "notified",
            statusInProcess = "in_process", statusSolved = "solved",
            statusUnsolved = "unsolved", statusClosed = "closed";


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
        radioGroup = findViewById(R.id.scale);
        radioGroup.setVisibility(View.GONE);



        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        type = findViewById(R.id.problem_type);
        description = findViewById(R.id.problem_description);
        //rating = findViewById(R.id.rating);
        result_user_comment = findViewById(R.id.problem_result_user_comment);

        mProblemImg = findViewById(R.id.imgView_status_pic);
        acceptBtn = findViewById(R.id.accept);
        rejectBtn = findViewById(R.id.reject);

        //rating.setText(getIntent().getStringExtra("problem_rating"));
        address.setText(getIntent().getStringExtra("problem_address"));

        setStatusPic();

        if(getIntent().getStringExtra("problem_scale") == null) {
            radioGroup.setVisibility(View.VISIBLE);
            result_user_comment.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("user_result_comment") != null) {
            result_user_comment.setText(getIntent().getStringExtra("user_result_comment"));
        } else {
            result_user_comment.setText("Комментарий пользователя отсутствует");
        }
        rejectBtn.setOnClickListener(view -> {
            eventStatus = "rejected";
            ApplicationService.getInstance()
            .getJSONApi()
            .updateEvent(token, eventId,"",eventStatus, "rejected", scale, personId)
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
                    .updateEvent(token, eventId,"",eventStatus, "accepted", scale, personId)
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

    void setStatusPic(){
        if (eventStatus.equals(statusCreated)){
            mProblemImg.setImageResource(R.drawable.status_pic_unsolved);

        } else if (eventStatus.equals(statusInProcess) || eventStatus.equals(statusNotified)) {
            mProblemImg.setImageResource(R.drawable.status_pic_in_progress);

        } else if (eventStatus.equals(statusInit)) {
            mProblemImg.setImageResource(R.drawable.status_pic_init);

        } else if (eventStatus.equals(statusSolved)) {
            mProblemImg.setImageResource(R.drawable.status_pic_solved);

        } else if (eventStatus.equals(statusUnsolved) || eventStatus.equals(statusClosed)) {
            mProblemImg.setImageResource(R.drawable.status_pic_closed);
        }
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.small_rb:
                if (checked)
                    scale = "small";
                    break;
            case R.id.medium_rb:
                if (checked)
                    scale = "medium";
                    break;
            case R.id.large_rb:
                if (checked)
                    scale = "large";
                    break;
        }
    }
}
