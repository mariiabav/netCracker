package com.example.problemsolver.organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;


import com.example.problemsolver.organization.model.Address;
import com.example.problemsolver.organization.model.Area;
import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.example.problemsolver.R;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SuggestItem;
import com.yandex.mapkit.search.SuggestOptions;
import com.yandex.mapkit.search.SuggestSession;
import com.yandex.mapkit.search.SuggestType;
import com.yandex.runtime.Error;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrganizationActivity extends AppCompatActivity implements SuggestSession.SuggestListener{

    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";
    private final int RESULT_NUMBER_LIMIT = 5;

    private final BoundingBox BOUNDING_BOX = new BoundingBox(
            new Point(60.092945, 29.961734),
            new Point(59.705141, 30.787196));
    private final SuggestOptions SEARCH_OPTIONS = new SuggestOptions().setSuggestTypes(
            SuggestType.GEO.value);

    private SuggestSession suggestSession;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;
    private ListView suggestResultView;

    private EditText name, description, email, number;
    private Spinner spinner;

    private String orgName, orgDescription, orgEmail, orgPhone, address;
    private Area orgArea;
    private Address orgAddress;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_organization);

        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);

        SearchManager searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        suggestSession = searchManager.createSuggestSession();

        SharedPreferences settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT", "");

        name = findViewById(R.id.editText_new_org_name);
        description = findViewById(R.id.editText_new_org_desc);
        email = findViewById(R.id.editText_new_org_email);
        number = findViewById(R.id.editText_new_org_number);
        suggestResultView = findViewById(R.id.suggest_result);
        spinner = findViewById(R.id.spinner);

        EditText queryEdit = findViewById(R.id.editText_new_org_address);
        ArrayAdapter<String> adapter;
        String[] array = getResources().getStringArray(R.array.areas);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        spinner.setAdapter(adapter);

        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                suggestResult);
        suggestResultView.setAdapter(resultAdapter);


        Button registerOrgBtn = findViewById(R.id.btn_signup);

        queryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                requestSuggest(editable.toString());
            }
        });

        suggestResultView.setOnItemClickListener((parent, view, position, id) -> {
            String strSelectedFeature = (String) parent.getAdapter().getItem(position);
            queryEdit.setText(strSelectedFeature);

        });

        registerOrgBtn.setOnClickListener(view -> {
            orgName = name.getText().toString();
            orgDescription = description.getText().toString();
            orgEmail = email.getText().toString();
            orgPhone = number.getText().toString();
            address = queryEdit.getText().toString();

            String[] splittedAddress = address.split(", ");
            orgArea = new Area(spinner.getSelectedItem().toString());
            orgAddress = new Address(splittedAddress[2], splittedAddress[3], orgArea);

            RegisteredOrganization registeredOrganization = new RegisteredOrganization(orgAddress, orgName, orgDescription, orgEmail, orgPhone);
            ApplicationService.getInstance()
                    .getJSONApi()
                    .postRegistedOrgData(token, registeredOrganization)
                    .enqueue(new Callback<RegisteredOrganization>() {
                        @Override
                        public void onResponse(@NonNull Call<RegisteredOrganization> call, @NonNull Response<RegisteredOrganization> response) {
                            if (response.isSuccessful()) {
                                showMessage("Регистрация организации вполнена успешно.");
                            } else {
                                showMessage("Сервер вернул ошибку. Регистрация организации не вполнена");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<RegisteredOrganization> call, @NonNull Throwable t) {
                            showMessage("Ошибка во время выполнения запроса. Организация не ыбла создана. ");
                        }
                    });
        });
    }

    @Override
    protected void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onResponse(@NonNull List<SuggestItem> suggest) {
        suggestResult.clear();
        for (int i = 0; i < Math.min(RESULT_NUMBER_LIMIT, suggest.size()); i++) {
            suggestResult.add(suggest.get(i).getDisplayText());
        }
        resultAdapter.notifyDataSetChanged();
        suggestResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(@NonNull Error error) {

    }

    private void requestSuggest(String query) {
        suggestResultView.setVisibility(View.INVISIBLE);
        suggestSession.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this);
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
