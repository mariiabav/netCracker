package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.problemsolver.event.Model.Event;
import com.example.problemsolver.event.Model.Problem;
import com.example.problemsolver.profile.ProfileActivity;

public class ProblemResultActivity extends AppCompatActivity {

    private Button resultBtn;
    private EditText resultComment;
    private ImageView resultPhoto;
    private String token;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_result);
        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT", "");
        String problemId = getIntent().getStringExtra("problem_id");
        Problem problem = new Problem(problemId);

        resultBtn = findViewById(R.id.btn_result);
        resultComment = findViewById(R.id.problem_result_comment);
        resultPhoto = findViewById(R.id.imageView_result);

        resultBtn.setOnClickListener(view -> {
            Event event = new Event(resultComment.getText().toString(), "solved", problem);
            solvedProblemRequest(event);
        });

    }

    private void solvedProblemRequest(Event event) {
        ApplicationService.getInstance()
                .getJSONApi()
                .createEvent(token, event)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        showMessage("Решение проблемы успешно отправлено");
                        Intent intent = new Intent(ProblemResultActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }


}
