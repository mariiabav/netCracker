package com.example.problemsolver.Problem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class NewProblemActivity extends Activity implements SuggestSession.SuggestListener {

    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";
    private final int RESULT_NUMBER_LIMIT = 5;

    private SearchManager searchManager;
    private SuggestSession suggestSession;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;
    private ListView suggestResultView;

    private final Point CENTER = new Point(55.75, 37.62);
    private final BoundingBox BOUNDING_BOX = new BoundingBox(
            new Point(60.092945, 29.961734),
            new Point(59.705141, 30.787196));
    private final SuggestOptions SEARCH_OPTIONS =  new SuggestOptions().setSuggestTypes(
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

        final EditText queryEdit = (EditText)findViewById(R.id.suggest_query);
        suggestResultView = (ListView)findViewById(R.id.suggest_result);

        suggestResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String strSelectedFeature = (String)parent.getAdapter().getItem(position);
                queryEdit.setText(strSelectedFeature);
                suggestResult.clear();
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

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

}
