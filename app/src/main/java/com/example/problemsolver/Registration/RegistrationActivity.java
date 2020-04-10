package com.example.problemsolver.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, surname, email, number, password;
    private Button register, login, date, areaList;
    TextView areas;

    private String firstName, secondName, email1, phone, pass, birthday;
    private List<Area> personAreas =  new ArrayList<>();

    TextView currentDateTime;
    Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        currentDateTime = findViewById(R.id.currentDateTime);
        name = findViewById(R.id.signup_input_name);
        surname = findViewById(R.id.signup_input_surname);
        email = findViewById(R.id.signup_input_email);
        number = findViewById(R.id.signup_input_number);
        areas = findViewById(R.id.checked_areas);
        password = findViewById(R.id.signup_input_password);
        register = findViewById(R.id.btn_signup);
        login = findViewById(R.id.btn_link_login);
        areaList = findViewById(R.id.btn_link_area_list);

        areaList.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationActivity.this, AreasActivity.class);

            intent.putExtra("name",  name.getText().toString());
            intent.putExtra("lastName",   surname.getText().toString());
            intent.putExtra("email",  email.getText().toString());
            intent.putExtra("number", number.getText().toString());
            intent.putExtra("date",  DateUtils.formatDateTime(RegistrationActivity.this, dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));

            startActivity(intent);
        });

        final ArrayList<String> checkedAreas = getIntent().getStringArrayListExtra("checkedAreas");
        name.setText(getIntent().getStringExtra("name"));
        surname.setText(getIntent().getStringExtra("lastName"));
        email.setText(getIntent().getStringExtra("email"));
        number.setText(getIntent().getStringExtra("number"));
        //currentDateTime.setText(getIntent().getStringExtra("date"));




        String stringAreas = "";
        if (checkedAreas != null){
            for (String area: checkedAreas) {
                stringAreas += area.split(" ")[0] + "\n";
            }
            areas.setText(stringAreas);
        }

        register.setOnClickListener(view -> {
            firstName = name.getText().toString();
            secondName = surname.getText().toString();
            email1 = email.getText().toString();
            phone = number.getText().toString();
            pass = password.getText().toString();
            birthday = DateUtils.formatDateTime(RegistrationActivity.this, dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);

            String [] date = birthday.split("\\.");
            String day = date[0];
            String month = date[1];
            String year = date[2];
            String serverDate = year + "-" + month + "-" + day;

            List<Role> roles = new ArrayList<>();
            final Role role = new Role("123e4567-e89b-12d3-a456-426655440000", "ROLE_ADMIN");
            roles.add(role);

            for (String area: checkedAreas){
                personAreas.add(new Area(area));
            }



            final RegisteredPerson registeredPerson = new RegisteredPerson(firstName, secondName, email1, phone, pass, serverDate, roles, personAreas);
            //if (checkDataEntered()){
            ApplicationService.getInstance()
                    .getJSONApi()
                    .postRegistedPersonData(registeredPerson)
                    .enqueue(new Callback<RegisteredPerson>() {
                        @Override
                        public void onResponse(@NonNull Call<RegisteredPerson> call, @NonNull Response<RegisteredPerson> response) {
                            if (response.isSuccessful()){
                                //запрос выполнился успешно
                                showMessage("Регистрация вполнена успешно");
                            }
                            else {
                                //сервер вернул ошибку
                                showMessage("Регистрация не вполнена");
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<RegisteredPerson> call, @NonNull Throwable t) {
                            showMessage("Ошибка во время выполнения запроса");
                        }
                    });
            //}
        });
    }


    // диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(RegistrationActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    // установка начальных даты и времени
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(RegistrationActivity.this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d= (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTime();
    };

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean checkDataEntered(){
        if ((isEmpty(name)) && (isEmpty(surname)) && (isEmail(email))){
            return true;
        }
        return false;
    }
    private void showMessage(String string){
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
