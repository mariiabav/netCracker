package com.example.problemsolver.Event;

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

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Event.Model.Event;
import com.example.problemsolver.Event.Model.EventResponse;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;
import com.example.problemsolver.R;
import com.example.problemsolver.ServerApi;

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

public class EventActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private static final String TAG = "EventActivity";

    private PaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private Button btnRetry;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");

        rv = findViewById(R.id.main_recycler);
        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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

        serverApi = ApplicationService.getInstance().getJSONApi();

        loadFirstPage();

        btnRetry.setOnClickListener(view -> loadFirstPage());

        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
        }
        return super.onOptionsItemSelected(item);
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

        callServerApi().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                hideErrorView();

                List<Event> results = fetchResults(response);
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
            public void onFailure(Call<EventResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<Event> fetchResults(Response<EventResponse> response) {
        List<Event> eventList = response.body().getEvents();
        return eventList;
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callServerApi().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<Event> results = fetchResults(response);
                adapter.addAll(results);
                if(currentPage != total_pages) {
                    adapter.addLoadingFooter();
                }
                else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private Call<EventResponse> callServerApi() {
        return serverApi.getEvents(
                token,
                currentPage,
                8,
                "offerDate",
                "desc"
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
