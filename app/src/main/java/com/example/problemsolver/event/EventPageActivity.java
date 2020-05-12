package com.example.problemsolver.event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.R;
import com.example.problemsolver.problemFeed.page.ProblemPageActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPageActivity extends AppCompatActivity {


    private TextView address, date, type, description, result_user_comment;
    private Button acceptBtn, rejectBtn;
    private SharedPreferences settings;
    private String token, personId, problemId, eventStatus, eventId, result, moderatorId;
    private String scale = "scale";
    private RadioGroup radioGroup;
    private ImageView mProblemStatus;
    private ImageView [] problemImages = new ImageView [3];

    private String statusInit = "init", statusCreated = "created", statusNotified = "notified",
            statusInProcess = "in_process", statusSolved = "solved",
            statusUnsolved = "unsolved", statusClosed = "closed";

    private List<String> pictures = new ArrayList<>();


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



        problemImages[0] = findViewById(R.id.imgView_postPic1);
        problemImages[1] = findViewById(R.id.imgView_postPic2);
        problemImages[2] = findViewById(R.id.imgView_postPic3);
        problemImages[0].setVisibility(View.GONE);
        problemImages[1].setVisibility(View.GONE);
        problemImages[2].setVisibility(View.GONE);


        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        type = findViewById(R.id.problem_type);
        description = findViewById(R.id.problem_description);
        result_user_comment = findViewById(R.id.problem_result_user_comment);

        mProblemStatus = findViewById(R.id.imgView_status_pic);
        acceptBtn = findViewById(R.id.accept);
        rejectBtn = findViewById(R.id.reject);


        address.setText(getIntent().getStringExtra("problem_address"));
        if(eventStatus.equals("solved") && getIntent().getStringArrayListExtra("event_pictures_list") != null){
            pictures = getIntent().getStringArrayListExtra("event_pictures_list");
        }
        else if(getIntent().getStringArrayListExtra("problem_pictures_list") != null){
            pictures = getIntent().getStringArrayListExtra("problem_pictures_list");
        }
            int i = 0;

            if (pictures != null) {
                for (String imageUri : pictures) {
                    problemImages[i].setVisibility(View.VISIBLE);
                    Glide.with(EventPageActivity.this)
                            .load(imageUri)
                            .apply(new RequestOptions().fitCenter())
                            .into(problemImages[i]);
                    i++;
                }
            }





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
            mProblemStatus.setImageResource(R.drawable.status_pic_unsolved);

        } else if (eventStatus.equals(statusInProcess) || eventStatus.equals(statusNotified)) {
            mProblemStatus.setImageResource(R.drawable.status_pic_in_progress);

        } else if (eventStatus.equals(statusInit)) {
            mProblemStatus.setImageResource(R.drawable.status_pic_init);

        } else if (eventStatus.equals(statusSolved)) {
            mProblemStatus.setImageResource(R.drawable.status_pic_solved);

        } else if (eventStatus.equals(statusUnsolved) || eventStatus.equals(statusClosed)) {
            mProblemStatus.setImageResource(R.drawable.status_pic_closed);
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
