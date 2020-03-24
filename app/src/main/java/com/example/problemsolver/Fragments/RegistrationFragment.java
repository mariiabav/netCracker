package com.example.problemsolver.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
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
import com.example.problemsolver.Retrofit.NetworkService;
import com.example.problemsolver.Retrofit.RegistedPerson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends Fragment {

    private EditText name, surname, email, number, bithdate, password;
    private Button register, login;

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

    //
    private View.OnClickListener onRegistrationClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            //логикааа
            //хотя зачем, если можно в onCreateView?
        }
    };



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
            public void onClick(View view) {
                if (checkDataEntered()){
                    NetworkService.getInstance()
                            .getJSONApi()
                            .postRegistedPersonData(/*Должны отправить все данные челика внутри RegistedPerson!*/)
                            .enqueue(new Callback<RegistedPerson>() {
                                @Override
                                public void onResponse(@NonNull Call<RegistedPerson> call, @NonNull Response<RegistedPerson> response) {
                                    if (response.isSuccessful()){
                                        //запрос выполнился успешно
                                    }
                                    else {
                                        //сервер вернул ошибку
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<RegistedPerson> call, @NonNull Throwable t) {
                                    //ошибка во время выполнения запроса
                                }
                            });
                }

            }
        });

        return view;
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
    private void showMessege(@StringRes int string){
        Toast t = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
        t.show();
    }

}
