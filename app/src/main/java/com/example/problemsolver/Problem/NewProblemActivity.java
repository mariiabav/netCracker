package com.example.problemsolver.Problem;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.problemsolver.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Models.DistrictResponse.FeatureMember;
import com.example.problemsolver.Models.NewProblemResponse.RegionDataResponse;
import com.example.problemsolver.ProblemService;
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

public class NewProblemActivity extends Activity implements SuggestSession.SuggestListener {

    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";
    private final int RESULT_NUMBER_LIMIT = 5;

    private SearchManager searchManager;
    private SuggestSession suggestSession;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;
    private ListView suggestResultView;

    private Button problem;
    private Integer results = 1;
    private String format = "json";
    private EditText type, description, region;

    private String address;
    private String coordinates;
    private String adminAreaName;

    String problemType, problemDescription, problemRegion;


    String API_key = "7e3eee55-cf92-4361-919e-e1666d3df1d1";

    private final Point CENTER = new Point(55.75, 37.62);
    private final BoundingBox BOUNDING_BOX = new BoundingBox(
            new Point(60.092945, 29.961734),
            new Point(59.705141, 30.787196));
    private final SuggestOptions SEARCH_OPTIONS = new SuggestOptions().setSuggestTypes(
            SuggestType.GEO.value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);
        setContentView(R.layout.activity_new_problem);
        super.onCreate(savedInstanceState);

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        suggestSession = searchManager.createSuggestSession();

        final EditText queryEdit = (EditText) findViewById(R.id.suggest_query);
        suggestResultView = (ListView) findViewById(R.id.suggest_result);


        suggestResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String strSelectedFeature = (String) parent.getAdapter().getItem(position);
                queryEdit.setText(strSelectedFeature);
                address = strSelectedFeature;
                suggestResult.clear();
                ProblemService.getInstance()
                        .getJSONApi()
                        .getRegionData(API_key, format, address)
                        .enqueue(new Callback<RegionDataResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<RegionDataResponse> call, Response<RegionDataResponse> response) {
                                coordinates = response.body()
                                        .getResponse()
                                        .getGeoObjectCollection()
                                        .getFeatureMember()
                                        .get(0)
                                        .getGeoObject()
                                        .getPoint()
                                        .getPos();
                                showMessage(coordinates);
                            }

                            @Override
                            public void onFailure(@NonNull Call<RegionDataResponse> call, @NonNull Throwable t) {
                            }
                        });
            }
        });

        problem = findViewById(R.id.btn_problem);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                ProblemService.getInstance()
                        .getJSONApi()
                        .getDistrictName(API_key, format, coordinates)
                        .enqueue(new Callback<DistrictResponse>() {
                            @Override
                            public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                               List<FeatureMember> featureMembers = response.body().getResponse().getGeoObjectCollection()
                                        .getFeatureMember();
                               for(FeatureMember featureMember : featureMembers) {
                                   if(featureMember.getGeoObject()
                                   .getMetaDataProperty()
                                   .getGeocoderMetaData()
                                   .getKind().contains("district")) {
                                       adminAreaName = featureMember.getGeoObject()
                                               .getMetaDataProperty()
                                               .getGeocoderMetaData()
                                               .getAddressDetails()
                                               .getCountry()
                                               .getAdministrativeArea()
                                               .getLocality()
                                               .getDependentLocality()
                                               .getDependentLocalityName();
                                       break;
                                   }

                               }

                               showMessage(adminAreaName);

                            }

                            @Override
                            public void onFailure(Call<DistrictResponse> call, Throwable t) {

                            }
                        });
            }
        });

        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                suggestResult);
        suggestResultView.setAdapter(resultAdapter);

        queryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                requestSuggest(editable.toString());
            }
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
