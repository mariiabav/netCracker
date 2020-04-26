package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    }
}
