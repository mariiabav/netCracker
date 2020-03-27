package com.example.problemsolver.Main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.problemsolver.R;
import com.example.problemsolver.Retrofit.RegistrationService;
import com.example.problemsolver.Retrofit.RegistedPerson;
import com.example.problemsolver.Retrofit.Role;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends Fragment {

    private EditText name, surname, email, number, bithdate, password;
    private Button register, login;

    String firstName, secondName, email1, phone, pass, bithdate1;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        name = view.findViewById(R.id.signup_input_name);
        surname = view.findViewById(R.id.signup_input_surname);
        email = view.findViewById(R.id.signup_input_email);
        number = view.findViewById(R.id.signup_input_number);
        bithdate = view.findViewById(R.id.signup_input_bithdate);
        password = view.findViewById(R.id.signup_input_password);

        register = view.findViewById(R.id.btn_signup);
        login = view.findViewById(R.id.btn_link_login);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                firstName = name.getText().toString();
                secondName = surname.getText().toString();
                email1 = email.getText().toString();
                phone = number.getText().toString();
                pass = password.getText().toString();
                bithdate1 = "2020-03-24T12:17:23";

                List<Role> roles = new ArrayList<>();
                final Role role = new Role("123e4567-e89b-12d3-a456-426655440000", "ROLE_ADMIN");
                roles.add(role);

                final RegistedPerson registedPerson = new RegistedPerson(firstName, secondName, email1, phone, pass, bithdate1, roles);
                //if (checkDataEntered()){
                    RegistrationService.getInstance()
                            .getJSONApi()
                            .postRegistedPersonData(registedPerson)
                            .enqueue(new Callback<RegistedPerson>() {
                                @Override
                                public void onResponse(@NonNull Call<RegistedPerson> call, @NonNull Response<RegistedPerson> response) {
                                    if (response.isSuccessful()){
                                        //запрос выполнился успешно
                                        Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_loginFragment);
                                        showMessege("Регистрация вполнена успешно");
                                    }
                                    else {
                                        //сервер вернул ошибку
                                        showMessege("Регистрация не вполнена");
                                    }
                                }
                                @Override
                                public void onFailure(@NonNull Call<RegistedPerson> call, @NonNull Throwable t) {
                                    showMessege("Ошибка во время выполнения запроса");
                                }
                            });
                //}

            }
        });

        return view;
    }

    private void showMessege(String firstName, String secondName, String email1, String phone, String pass) {
    }

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
    private void showMessege(String string){
        Toast t = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
        t.show();
    }

}
