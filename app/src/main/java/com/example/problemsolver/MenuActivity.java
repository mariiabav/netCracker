package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.problemsolver.Feed.FeedActivity;
import com.example.problemsolver.Login.LoginActivity;
import com.example.problemsolver.Map.MapActivity;
import com.example.problemsolver.Organization.NewOrganizationActivity;
import com.example.problemsolver.Problem.NewProblemActivity;
import com.example.problemsolver.Profile.ProfileActivity;
import com.example.problemsolver.Registration.RegistrationActivity;

public class MenuActivity extends AppCompatActivity {

    private Button register, login, problem, feed, map, organization, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //register = findViewById(R.id.btn_first);
        //login = findViewById(R.id.btn_second);
        problem = findViewById(R.id.btn_third);
        feed = findViewById(R.id.btn_forth);
        map = findViewById(R.id.btn_fifth);
        organization = findViewById(R.id.btn_sixth);
        profile = findViewById(R.id.btn_seventh);


        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, NewProblemActivity.class);
                startActivity(intent);
            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, NewOrganizationActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}
