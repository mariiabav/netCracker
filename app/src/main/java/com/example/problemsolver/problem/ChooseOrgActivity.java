package com.example.problemsolver.problem;


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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.organization.model.FeedOrgResponse;
import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.example.problemsolver.R;
import com.example.problemsolver.ServerApi;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;

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

public class ChooseOrgActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private static final String TAG = "FeedOrgActivity";
    private static final int PAGE_START = 0;

    private OrgPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private TextView txtError;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int currentPage = PAGE_START;

    private ServerApi serverApi;
    private String token;
    private String personId;
    private Boolean onlyMyAreas = false;
    private String sortBy = "name";
    private String adminArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_org);
        SharedPreferences settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        Button btnRetry = findViewById(R.id.error_btn_retry);
        adminArea = getIntent().getStringExtra("admin_area");

        serverApi = ApplicationService.getInstance().getJSONApi();
        token = settings.getString("JWT","");
        personId = settings.getString("id","");
        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        txtError = findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        adapter = new OrgPaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView rv = findViewById(R.id.main_recycler);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

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
        return serverApi.getAreaOrgs(
                token,
                currentPage,
                8,
                sortBy,
                "desc",
                adminArea
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

