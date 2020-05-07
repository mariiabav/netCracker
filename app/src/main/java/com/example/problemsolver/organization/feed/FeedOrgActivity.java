package com.example.problemsolver.organization.feed;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.organization.model.FeedOrgResponse;
import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;
import com.example.problemsolver.R;
import com.example.problemsolver.ServerApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeoutException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedOrgActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private static final String TAG = "FeedOrgActivity";
    private static final int PAGE_START = 0;

    private OrgPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private Button allAreasBtn, myAreasBtn, solvedTopBtn, inProcessTopBtn, unsolvedTopBtn;
    private TextView txtError;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int currentPage = PAGE_START;

    private ServerApi serverApi;
    private String token;
    private String personId;
    private Boolean onlyMyAreas = false;
    private String sortBy = "solvedProblemsCount";
    private String flag;
    private String adminArea;

    private CheckBox checkBoxCreated;
    private CheckBox checkBoxInProcess;
    private CheckBox checkBoxSolved;
    private CheckBox checkBoxAllAreas;
    private CheckBox checkBoxMyAreas;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teest);
        //setContentView(R.layout.test_org);

        flag = getIntent().getStringExtra("flag");
        adminArea = getIntent().getStringExtra("admin_area");

        SharedPreferences settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        Button btnRetry = findViewById(R.id.error_btn_retry);

        serverApi = ApplicationService.getInstance().getJSONApi();
        token = settings.getString("JWT","");
        personId = settings.getString("id","");
        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        txtError = findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        allAreasBtn = findViewById(R.id.all_area_btn);
        myAreasBtn = findViewById(R.id.my_area_btn);

        solvedTopBtn = findViewById(R.id.solved_top_btn);
        inProcessTopBtn = findViewById(R.id.in_proccess_top_btn);
        unsolvedTopBtn = findViewById(R.id.unsolved_top_btn);

        adapter = new OrgPaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView rv = findViewById(R.id.main_recycler);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        allAreasBtn.setOnClickListener(view -> {

            allAreasBtn.setBackgroundColor(getResources().getColor(R.color.red));
            allAreasBtn.setTextColor(getResources().getColor(R.color.white));

            myAreasBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            myAreasBtn.setTextColor(getResources().getColor(R.color.vinous));

            onlyMyAreas = false;

            swipeRefreshLayout.setRefreshing(true);
            doRefresh();
        });

        myAreasBtn.setOnClickListener(view -> {

            myAreasBtn.setBackgroundColor(getResources().getColor(R.color.red));
            myAreasBtn.setTextColor(getResources().getColor(R.color.white));

            allAreasBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            allAreasBtn.setTextColor(getResources().getColor(R.color.vinous));

            onlyMyAreas = true;

            swipeRefreshLayout.setRefreshing(true);
            doRefresh();
        });

        solvedTopBtn.setOnClickListener(view -> {
            sortBy = "solvedProblemsCount";

            solvedTopBtn.setBackgroundColor(getResources().getColor(R.color.red));
            solvedTopBtn.setTextColor(getResources().getColor(R.color.white));

            inProcessTopBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            inProcessTopBtn.setTextColor(getResources().getColor(R.color.vinous));

            unsolvedTopBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            unsolvedTopBtn.setTextColor(getResources().getColor(R.color.vinous));

            showMessage("решенные");
            swipeRefreshLayout.setRefreshing(true);
            doRefresh();
        });

        inProcessTopBtn.setOnClickListener(view -> {
            sortBy = "inProcessProblemsCount";

            inProcessTopBtn.setBackgroundColor(getResources().getColor(R.color.red));
            inProcessTopBtn.setTextColor(getResources().getColor(R.color.white));

            solvedTopBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            solvedTopBtn.setTextColor(getResources().getColor(R.color.vinous));

            unsolvedTopBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            unsolvedTopBtn.setTextColor(getResources().getColor(R.color.vinous));

            showMessage("организация решает");
            swipeRefreshLayout.setRefreshing(true);
            doRefresh();
        });

        unsolvedTopBtn.setOnClickListener(view -> {
            sortBy = "unsolvedProblemsCount";

            unsolvedTopBtn.setBackgroundColor(getResources().getColor(R.color.red));
            unsolvedTopBtn.setTextColor(getResources().getColor(R.color.white));

            inProcessTopBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            inProcessTopBtn.setTextColor(getResources().getColor(R.color.vinous));

            solvedTopBtn.setBackgroundColor(getResources().getColor(R.color.light_grey));
            solvedTopBtn.setTextColor(getResources().getColor(R.color.vinous));

            showMessage("просрочены или нерешенные");
            swipeRefreshLayout.setRefreshing(true);
            doRefresh();
        });

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();

        btnRetry.setOnClickListener(view -> loadFirstPage());

        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_org, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_org_refresh:
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callServerApi().isExecuted())
            callServerApi().cancel();

        adapter.getOrgs().clear();
        adapter.notifyDataSetChanged();
        currentPage = 0;
        isLastPage = false;
        loadFirstPage();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        hideErrorView();
        currentPage = PAGE_START;

        callServerApi().enqueue(new Callback<FeedOrgResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedOrgResponse> call, @NotNull Response<FeedOrgResponse> response) {
                hideErrorView();

                List<RegisteredOrganization> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);
                adapter.addSortBy(sortBy);
                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedOrgResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<RegisteredOrganization> fetchResults(Response<FeedOrgResponse> response) {
        return response.body().getOrganizationList();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callServerApi().enqueue(new Callback<FeedOrgResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedOrgResponse> call, @NotNull Response<FeedOrgResponse> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<RegisteredOrganization> results = fetchResults(response);
                adapter.addAll(results);
                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedOrgResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private Call<FeedOrgResponse> callServerApi() {
        return serverApi.getOrgs(
                token,
                currentPage,
                8,
                sortBy,
                "desc",
                personId,
                onlyMyAreas
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void showMessage(String string){
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}

