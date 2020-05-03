package com.example.problemsolver.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.ServerApi;
import com.example.problemsolver.authorized.AuthorizedPerson;
import com.example.problemsolver.authorized.PersonArea;
import com.example.problemsolver.event.EventActivity;
import com.example.problemsolver.login.LoginActivity;
import com.example.problemsolver.R;
import com.example.problemsolver.problemFeed.model.Feed2Problem;
import com.example.problemsolver.problemFeed.model.FeedResponse;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;

import java.util.List;
import java.util.concurrent.TimeoutException;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements PaginationAdapterCallback {
    private static final String TAG = "FeedActivity";

    private ProblemPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rv;
    private LinearLayout errorLayout;
    private Button btnRetry;
    private TextView txtError;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int PAGE_START = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;

    private TextView FScView, emailView, numberView, dateView, area1View, area2View, area3View, profileStatus;
    private Button myInfo, myProblems, eventsBtn, complainBtn, resultBtn;
    private AuthorizedPerson authorizedPerson;
    private String FSc, email, number, date, area1, area2, area3, role;
    private TextView [] areaView = new TextView[3];

    private RelativeLayout myInfoRellay;
    private FrameLayout myProfileFeed;

    private List<PersonArea> personAreas;

    private String token;
    private String personId;
    private SharedPreferences settings;
    private ServerApi serverApi;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        personId = settings.getString("id","");

        FScView = findViewById(R.id.profile_name);

        emailView = findViewById(R.id.profile_email);
        numberView = findViewById(R.id.profile_number);
        dateView = findViewById(R.id.profile_date);
        area1View = findViewById(R.id.profile_area1);
        area2View = findViewById(R.id.profile_area2);
        area3View = findViewById(R.id.profile_area3);
        profileStatus = findViewById(R.id.profile_status);
        eventsBtn = findViewById(R.id.btn_events);


        eventsBtn.setVisibility(View.INVISIBLE);

        myInfo = findViewById(R.id.btn_info);
        myProblems = findViewById(R.id.btn_my_problems);

        myInfoRellay = findViewById(R.id.my_info_rellay);
        myProfileFeed = findViewById(R.id.frame_recycler_view);

        myInfoRellay.setVisibility(View.VISIBLE);
        myProfileFeed.setVisibility(View.INVISIBLE);

        eventsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EventActivity.class);
            startActivity(intent);
        });

        myProblems.setOnClickListener(view -> {
            myInfoRellay.setVisibility(View.INVISIBLE);
            myProfileFeed.setVisibility(View.VISIBLE);
        });

        myInfo.setOnClickListener(view -> {
            myInfoRellay.setVisibility(View.VISIBLE);
            myProfileFeed.setVisibility(View.INVISIBLE);
        });

        //ЭТО ДОЛЖНО БЫТЬ В АДАПТЕРЕ ДЛЯ КАЖДОГО item ЛЕНТЫ. Пока не уверена, как это будет работать. Нужна лента
        /*
        complainBtn = findViewById(R.id.btn_complain);
        resultBtn = findViewById(R.id.btn_result);

        complainBtn.setOnClickListener(view -> {
            //переход по ссылке
        });

        resultBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProblemResultActivity.class);
            startActivity(intent);
        });
*/
        personInfoRequest();

        rv = findViewById(R.id.problem_recycler);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        adapter = new ProblemPaginationAdapter(this);

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

        btnRetry.setOnClickListener(view -> loadFirstPage());

        loadFirstPage();

        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

    }

    private void personInfoRequest() {
        ApplicationService.getInstance()
                .getJSONApi()
                .getAuthorizedPersonInfo(token, personId)
                .enqueue(new Callback<AuthorizedPerson>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthorizedPerson> call, @NonNull Response<AuthorizedPerson> response) {
                        if (response.isSuccessful()) {
                            role = settings.getString("Roles", "");
                            if(role.equals("ROLE_ADMIN")) {
                                eventsBtn.setVisibility(View.VISIBLE);
                                profileStatus.setText("Администратор");
                            }
                            else if(role.equals("ROLE_USER")) {
                               eventsBtn.setVisibility(View.GONE);
                                profileStatus.setText("Пользователь");
                            }

                            authorizedPerson = response.body();
                            FSc = authorizedPerson.getFirstName() + " " + authorizedPerson.getSecondName();
                            email = authorizedPerson.getEmail();
                            number = authorizedPerson.getPhone();
                            date = authorizedPerson.getBirthDate();
                            personAreas = authorizedPerson.getPersonAreas();


                            FScView.setText(FSc);
                            emailView.setText(email);
                            numberView.setText(number);
                            dateView.setText(date);


                            areaView[0] = area1View;
                            areaView[1] = area2View;
                            areaView[2] = area3View;

                            int i = 0;
                            for (PersonArea personArea: personAreas){
                                areaView[i].setText(personArea.getAreaName());
                                i++;
                            }
                        } else {
                            showMessage("Сервер вернул ошибку. Информация о пользователе не получена.");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AuthorizedPerson> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса. Информация о пользователе не получена.");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
                break;
            case R.id.menu_exit:
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                settings.edit().remove("JWT").remove("id").remove("Roles").apply();
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void doRefresh() {
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

        callServerApi().enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                hideErrorView();

                List<Feed2Problem> results = fetchResults(response);
                adapter.addAll(results);

                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }

            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<Feed2Problem> fetchResults(Response<FeedResponse> response) {
        List<Feed2Problem> feed2ProblemList = response.body().getFeed2ProblemList();
        return feed2ProblemList;
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callServerApi().enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<Feed2Problem> results = fetchResults(response);
                adapter.addAll(results);

                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private Call<FeedResponse> callServerApi() {
        return serverApi.getMyProblems(
                token,
                currentPage,
                3,
                "rate",
                "desc",
                personId
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
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
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
