package com.example.problemsolver.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.problemsolver.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView FSc, email, number, date;
    private Button myInfo, myProblems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FSc = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        number = findViewById(R.id.profile_number);
        date = findViewById(R.id.profile_date);
    }
}
