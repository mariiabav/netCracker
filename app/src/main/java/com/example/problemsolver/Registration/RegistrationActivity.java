package com.example.problemsolver.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    private Spinner areas;
    private Button register, login, date;

    private String firstName, secondName, email1, phone, pass, bithday;
    private List<String> personAreas;

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


        password = findViewById(R.id.signup_input_password);
        areas = findViewById(R.id.spinner);

        register = findViewById(R.id.btn_signup);
        login = findViewById(R.id.btn_link_login);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                firstName = name.getText().toString();
                secondName = surname.getText().toString();
                email1 = email.getText().toString();
                phone = number.getText().toString();
                pass = password.getText().toString();
                bithday = DateUtils.formatDateTime(RegistrationActivity.this, dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
                showMessage(bithday);

                List<Role> roles = new ArrayList<>();
                final Role role = new Role("123e4567-e89b-12d3-a456-426655440000", "ROLE_ADMIN");
                roles.add(role);

                ArrayAdapter<?> adapter =
                        ArrayAdapter.createFromResource(RegistrationActivity.this, R.array.areas, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                areas.setAdapter(adapter);
                //TODO: сделать hint, множественный выбор из списка районов

                //дата рождения здесь через точку  bithday = день.месяц.год
                final RegisteredPerson registeredPerson = new RegisteredPerson(firstName, secondName, email1, phone, pass, bithday, roles, personAreas);
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


            }
        });

    }


    // отображаем диалоговое окно для выбора даты
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
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
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
