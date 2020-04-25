package com.example.problemsolver.Organization;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Login.LoginActivity;
import com.example.problemsolver.Organization.model.FeedOrgResponse;
import com.example.problemsolver.Organization.model.RegisteredOrganization;
import com.example.problemsolver.Registration.Area;
import com.example.problemsolver.Registration.RegisteredPerson;
import com.example.problemsolver.Registration.RegistrationActivity;
import com.example.problemsolver.Registration.Role;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;
import com.example.problemsolver.R;
import com.example.problemsolver.ServerApi;

import java.util.List;
import java.util.concurrent.TimeoutException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedOrgActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private static final String TAG = "EventActivity";

    private PaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private Button btnRetry, allAreas, myAreas, solvedTop, inProccessTop, unsolvedTop;
    private TextView txtError;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int PAGE_START = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<String> arrayList;

    private int total_pages;
    private int currentPage = PAGE_START;

    private ServerApi serverApi;
    private SharedPreferences settings;
    private String token;
    private String personId;

    private Boolean onlyMyAreas = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_org);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        personId = settings.getString("id","");

        rv = findViewById(R.id.main_recycler);
        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        allAreas = findViewById(R.id.all_area_btn);
        myAreas = findViewById(R.id.my_area_btn);

        solvedTop = findViewById(R.id.solved_top_btn);
        inProccessTop = findViewById(R.id.in_proccess_top_btn);
        unsolvedTop = findViewById(R.id.unsolved_top_btn);

        solvedTop.setVisibility(View.INVISIBLE);
        inProccessTop.setVisibility(View.INVISIBLE);
        unsolvedTop.setVisibility(View.INVISIBLE);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        allAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onlyMyAreas = false;
                showMessage("false");
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
            }
        });

        myAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onlyMyAreas = true;
                showMessage("true");
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
            }
        });

        solvedTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                showMessage("решенные");
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
            }
        });

        inProccessTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                showMessage("организация решает");
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
            }
        });

        unsolvedTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onlyMyAreas = true;

                showMessage("просрочены или нерешенные");
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
            }
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

        serverApi = ApplicationService.getInstance().getJSONApi();

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
            case R.id.menu_org_stat:
                showMessage("топ");
                solvedTop.setVisibility(View.VISIBLE);
                inProccessTop.setVisibility(View.VISIBLE);
                unsolvedTop.setVisibility(View.VISIBLE);

                allAreas.setVisibility(View.INVISIBLE);
                myAreas.setVisibility(View.INVISIBLE);
                break;
            case R.id.menu_org_list:
                showMessage("все огранизации");

                solvedTop.setVisibility(View.INVISIBLE);
                inProccessTop.setVisibility(View.INVISIBLE);
                unsolvedTop.setVisibility(View.INVISIBLE);

                allAreas.setVisibility(View.VISIBLE);
                myAreas.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String string){
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callServerApi().isExecuted())
            callServerApi().cancel();

        adapter.getProblems().clear();
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
            public void onResponse(Call<FeedOrgResponse> call, Response<FeedOrgResponse> response) {
                hideErrorView();

                List<RegisteredOrganization> results = fetchResults(response);
                total_pages = response.body().getPagesLimit();
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);
                if(currentPage <= total_pages) {
                    adapter.addLoadingFooter();
                }
                else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<FeedOrgResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<RegisteredOrganization> fetchResults(Response<FeedOrgResponse> response) {
        List<RegisteredOrganization> eventList = response.body().getOrganizationList();
        return eventList;
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callServerApi().enqueue(new Callback<FeedOrgResponse>() {
            @Override
            public void onResponse(Call<FeedOrgResponse> call, Response<FeedOrgResponse> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<RegisteredOrganization> results = fetchResults(response);
                adapter.addAll(results);
                if(currentPage != total_pages) {
                    adapter.addLoadingFooter();
                }
                else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<FeedOrgResponse> call, Throwable t) {
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
                "name",
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
}

