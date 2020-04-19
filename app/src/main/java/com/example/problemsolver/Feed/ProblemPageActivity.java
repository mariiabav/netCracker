package com.example.problemsolver.Feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Assessment;
import com.example.problemsolver.Feed.model.MyAssessmentResponse;
import com.example.problemsolver.R;

import java.util.Objects;

public class ProblemPageActivity extends AppCompatActivity {




    private ImageView imageLikes, imageDislikes;

    private TextView address, date, type, description, likes, dislikes;
    private ImageView status;
    private Button supportBtn;
    private static final String statusCreated = "created";
    private static final String statusInProcess = "in process";
    private static final String statusSolved = "solved";
    private static final String statusRejected = "rejected";
    private SharedPreferences settings;
    private String token, personId, problemId;

    private RelativeLayout likesRelley, dislikesRelley;

    private boolean pressedLikeBtn = false,  pressedDislikeBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_page);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        personId = settings.getString("id", "");
        problemId = getIntent().getStringExtra("problem_id");


        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        type = findViewById(R.id.problem_type);
        description = findViewById(R.id.problem_description);
        status = findViewById(R.id.status_pic);
        supportBtn = findViewById(R.id.btn_support);


        likesRelley = findViewById(R.id.like_not_btn);
        dislikesRelley = findViewById(R.id.dislike_not_btn);

        likes = findViewById(R.id.likes);
        dislikes = findViewById(R.id.dislikes);

        imageLikes = findViewById(R.id.heart);
        imageDislikes = findViewById(R.id.broken_heart);

        myAssessmentRequest();


        likesRelley.setOnClickListener(view -> {
           if (pressedLikeBtn){
               imageLikes.setImageResource(R.drawable.heart);
               pressedLikeBtn = false;
               likeRequest(token, personId, problemId);
           } else {
               if (pressedDislikeBtn){
                   imageDislikes.setImageResource(R.drawable.broken);
                   pressedDislikeBtn = false;
               }
               imageLikes.setImageResource(R.drawable.heart_red);
               pressedLikeBtn = true;
               likeRequest(token, personId, problemId);
           }
        });

        dislikesRelley.setOnClickListener(view -> {
            if (pressedDislikeBtn){
                imageDislikes.setImageResource(R.drawable.broken);
                pressedDislikeBtn = false;
                dislikeRequest(token, personId, problemId);
                //обновляем текствью
            } else {
                if (pressedLikeBtn){
                    imageLikes.setImageResource(R.drawable.heart);
                    pressedLikeBtn = false;
                }

                imageDislikes.setImageResource(R.drawable.broken_black);
                pressedDislikeBtn =  true;
                dislikeRequest(token, personId, problemId);
            }
        });

        supportBtn.setOnClickListener(view -> {
            ApplicationService.getInstance()
            .getJSONApi()
            .subscribe(token, problemId, personId)
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    showMessage("Подписался");
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
        assessmentRequest(token, problemId);
        //dislikes.setText("dislikes: " + getIntent().getStringExtra("problem_dislikes"));

        String serverStatus = getIntent().getStringExtra("problem_status");

        if (serverStatus.equals("created")){

            status.setImageResource(R.drawable.red_circle);
        } else if (serverStatus.equals("in process")) {
            status.setImageResource(R.drawable.yellow_circle);
        } else if (serverStatus.equals("solved")) {
            status.setImageResource(R.drawable.green_cirle);
        }
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    private void likeRequest(String token, String personId, String problemId){
        ApplicationService.getInstance()
                .getJSONApi()
                .setLike(token, problemId, personId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            showMessage("Лайк поставлен/снят успешно");
                            assessmentRequest(token, problemId);
                        }
                        else {
                            //сервер вернул ошибку
                            showMessage("Лайк не поставлен, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        //showMessage("Ошибка во время выполнения запроса: лайк");
                    }
                });
    }

    private void dislikeRequest(String token, String personId, String problemId){
        ApplicationService.getInstance()
                .getJSONApi()
                .setDislike(token, problemId, personId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            //запрос выполнился успешно
                            showMessage("Дизайк поставлен/снят успешно");
                            assessmentRequest(token, problemId);
                        }
                        else {
                            //сервер вернул ошибку
                            showMessage("Дизайк не поставлен, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: дизлайк");
                    }
                });
    }

    private void assessmentRequest(String token, String problemId){
        ApplicationService.getInstance()
                .getJSONApi()
                .getAssessment(token, problemId)
                .enqueue(new Callback<Assessment>() {
                    @Override
                    public void onResponse(@NonNull Call<Assessment> call, @NonNull Response<Assessment> response) {
                        if (response.isSuccessful()){
                            Assessment assessments = response.body();
                            //запрос выполнился успешно
                            likes.setText("likes: " + assessments.getLikesCount());
                            dislikes.setText("dislikes: " + assessments.getDislikesCount());
                            showMessage("Оценки получены успешно");
                        }
                        else {
                            //сервер вернул ошибку
                            showMessage("Оценки не получены, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Assessment> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: оценка");
                    }
                });
    }

    private void myAssessmentRequest(){
        ApplicationService.getInstance()
                .getJSONApi()
                .getMyAssessment(token, problemId, personId)
                .enqueue(new Callback<MyAssessmentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MyAssessmentResponse> call, @NonNull Response<MyAssessmentResponse> response) {
                        if (response.isSuccessful()){
                            String myAssessment = response.body().getResponse();
                            showMessage(myAssessment);
                            //запрос выполнился успешно
                            if(myAssessment.equals("like")){
                                imageLikes.setImageResource(R.drawable.heart_red);
                                pressedLikeBtn = true;
                            }
                            else if(myAssessment.equals("dislike")){
                                imageDislikes.setImageResource(R.drawable.broken_black);
                                pressedDislikeBtn = true;
                            }
                        }
                        else {
                            //сервер вернул ошибку
                            showMessage("Оценки не получены, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<MyAssessmentResponse> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: оценка");
                    }
                });
    }
}
