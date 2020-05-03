package com.example.problemsolver;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.problemsolver.problemFeed.FeedActivity;
import com.example.problemsolver.map.MapActivity;
import com.example.problemsolver.organization.feed.FeedOrgActivity;
import com.example.problemsolver.organization.NewOrganizationActivity;
import com.example.problemsolver.problem.NewProblemActivity;
import com.example.problemsolver.profile.ProfileActivity;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashboardActivity extends AppCompatActivity {


    private RelativeLayout problem, feed, map, organization, profile, set;
    private SharedPreferences settings;
    private String role, FCMtoken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }
            FCMtoken = task.getResult().getToken();
        });

        profile = findViewById(R.id.relay_profile);
        problem = findViewById(R.id.relay_new_problems);
        feed = findViewById(R.id.relay_feed);
        map = findViewById(R.id.relay_map);
        organization = findViewById(R.id.relay_org);
        set = findViewById(R.id.relay_settings);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        settings.edit().putString("FCM", FCMtoken).apply();
        role = settings.getString("Roles", "");

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

            if (role.equals("ROLE_ADMIN")) {
                Intent intent = new Intent(DashboardActivity.this, NewOrganizationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } else if (role.equals("ROLE_USER")) {
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
            Intent intent = new Intent(DashboardActivity.this, Main2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
