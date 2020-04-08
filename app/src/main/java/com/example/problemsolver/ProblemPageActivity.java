package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ProblemPageActivity extends AppCompatActivity {

    public static final String EXTRA_POS = "my_item_position";

    private TextView address, date, type, description, rating, status;
    private Button support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_page);

        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        type = findViewById(R.id.problem_type);
        description = findViewById(R.id.problem_description);
        rating = findViewById(R.id.rating);
        status = findViewById(R.id.status);

        int my_item_position = (int) getIntent().getExtras().get(EXTRA_POS);

    }
}
