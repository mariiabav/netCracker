package com.example.problemsolver.organization.feed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.R;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgPageActivity extends AppCompatActivity {

    private String token, personId, problemId;

    private String statusInit = "init", statusCreated = "created", statusNotified = "notified",
            statusInProcess = "in_process", statusSolved = "solved",
            statusUnsolved = "unsolved", statusClosed = "closed";

    private ImageView statusImg;
    private TextView address, date, type, description, rating;
    private Button supportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_page);

        SharedPreferences settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        personId = settings.getString("id", "");
        problemId = getIntent().getStringExtra("problem_id");


        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        type = findViewById(R.id.problem_type);
        description = findViewById(R.id.problem_description);
        rating = findViewById(R.id.rating);
        supportBtn = findViewById(R.id.btn_support);
        statusImg = findViewById(R.id.imgView_status_pic);

        supportBtn.setOnClickListener(view -> {
            ApplicationService.getInstance()
            .getJSONApi()
            .subscribe(token, problemId,personId)
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
        date.setText(getIntent().getStringExtra("problem_date").substring(0, 10));
        rating.setText("Рейтинг: " + getIntent().getStringExtra("problem_rating"));

        String serverStatus = getIntent().getStringExtra("problem_status");
        setStatusPic(serverStatus);

        //status.setText("Статус: " + serverStatus);
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    void setStatusPic(String serverStatus){
        if (serverStatus.equals(statusCreated)){
            statusImg.setImageResource(R.drawable.status_pic_unsolved);

        } else if (serverStatus.equals(statusInProcess) || serverStatus.equals(statusNotified)) {
            statusImg.setImageResource(R.drawable.status_pic_in_progress);

        } else if (serverStatus.equals(statusInit)) {
            statusImg.setImageResource(R.drawable.status_pic_init);

        } else if (serverStatus.equals(statusSolved)) {
            statusImg.setImageResource(R.drawable.status_pic_solved);

        } else if (serverStatus.equals(statusUnsolved) || serverStatus.equals(statusClosed)) {
            statusImg.setImageResource(R.drawable.status_pic_closed);
        }
    }
}
