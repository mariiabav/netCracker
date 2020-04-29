package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.problemsolver.event.Model.Event;

public class ProblemResultActivity extends AppCompatActivity {

    private Button resultBtn;
    private EditText resultComment;
    private ImageView resultPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_result);

        resultBtn = findViewById(R.id.btn_result);
        resultComment = findViewById(R.id.problem_result_comment);
        resultPhoto = findViewById(R.id.imageView_result);

        resultBtn.setOnClickListener(view -> {
            Event event = new Event();
        });

    }
        /*
    private void solvedProblemRequest(Event event) {
        ApplicationService.getInstance()
                .getJSONApi()
                .createEvent(token, event)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

         */
}
