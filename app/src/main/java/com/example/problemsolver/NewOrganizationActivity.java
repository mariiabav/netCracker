package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class NewOrganizationActivity extends AppCompatActivity {

    private EditText name, decsription, email, number;
    private Button registerOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_organization);

        name = findViewById(R.id.org_input_name);
        decsription = findViewById(R.id.org_input_desc);
        email = findViewById(R.id.org_input_email);
        number = findViewById(R.id.org_input_number);
        registerOrg = findViewById(R.id.btn_signup);
    }
}
