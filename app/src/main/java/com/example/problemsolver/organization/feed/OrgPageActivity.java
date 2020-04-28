package com.example.problemsolver.organization.feed;

import android.content.Context;
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

public class OrgPageActivity extends AppCompatActivity {

    private String token, personId, problemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_page);

        SharedPreferences settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        personId = settings.getString("id", "");
        problemId = getIntent().getStringExtra("problem_id");

        TextView address = findViewById(R.id.address);
        TextView date = findViewById(R.id.date);
        TextView type = findViewById(R.id.problem_type);
        TextView description = findViewById(R.id.problem_description);
        TextView rating = findViewById(R.id.rating);
        Button supportBtn = findViewById(R.id.btn_support);

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
        date.setText(getIntent().getStringExtra("problem_date"));
        rating.setText("Рейтинг: " + getIntent().getStringExtra("problem_rating"));


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
