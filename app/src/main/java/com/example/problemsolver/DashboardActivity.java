package com.example.problemsolver;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.problemsolver.ProblemFeed.FeedActivity;
import com.example.problemsolver.Map.MapActivity;
import com.example.problemsolver.Organization.FeedOrgActivity;
import com.example.problemsolver.Organization.NewOrganizationActivity;
import com.example.problemsolver.Problem.NewProblemActivity;
import com.example.problemsolver.Profile.ProfileActivity;

public class DashboardActivity extends AppCompatActivity {


    private RelativeLayout register, login, problem, feed, map, organization, profile, set;
    TextView orgVeiw;
    private SharedPreferences settings;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        profile = findViewById(R.id.rellay_profile);
        problem = findViewById(R.id.rellay_problems);
        feed = findViewById(R.id.rellay_feed);
        map = findViewById(R.id.rellay_map);
        organization = findViewById(R.id.rellay_org);
        set = findViewById(R.id.rellay_timeline);
        orgVeiw = findViewById(R.id.textview_org);


        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        role = settings.getString("Roles", "");
        System.out.println(role);

        if(role.equals("ROLE_ADMIN")) {
            orgVeiw.setText("Создать орг");
        }
        else if(role.equals("ROLE_USER")) {
            orgVeiw.setText("Список орг");
        }

        problem.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, NewProblemActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        feed.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, FeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        map.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, MapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        organization.setOnClickListener(view -> {

            if(role.equals("ROLE_ADMIN")) {
                Intent intent = new Intent(DashboardActivity.this, NewOrganizationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            else if(role.equals("ROLE_USER")) {
                Intent intent = new Intent(DashboardActivity.this, FeedOrgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }


        });

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        set.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, ImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
