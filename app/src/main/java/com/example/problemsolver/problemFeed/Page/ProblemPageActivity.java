package com.example.problemsolver.problemFeed.Page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Assessment;
import com.example.problemsolver.event.Model.Problem;
import com.example.problemsolver.problemFeed.model.Comment;
import com.example.problemsolver.problemFeed.model.CommentResponse;
import com.example.problemsolver.problemFeed.model.MyAssessmentResponse;
import com.example.problemsolver.problemFeed.model.Person;
import com.example.problemsolver.R;
import com.example.problemsolver.ServerApi;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class ProblemPageActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private ImageView imageLikes, imageDislikes;

    private TextView likes;
    private TextView dislikes;
    private String token;
    private String personId;
    private String problemId;

    private boolean pressedLikeBtn = false,  pressedDislikeBtn = false;

    private CommentPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView commentRecycler;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private Button btnRetry;
    private TextView txtError;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int PAGE_START = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int total_pages;
    private int currentPage = PAGE_START;

    private ServerApi serverApi;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_page);

        SharedPreferences settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String pictureId = getIntent().getStringExtra("picture_id");
        TextView address = findViewById(R.id.address);
        TextView date = findViewById(R.id.date);
        TextView type = findViewById(R.id.problem_type);
        TextView description = findViewById(R.id.problem_description);
        ImageView status = findViewById(R.id.status_pic);
        Button supportBtn = findViewById(R.id.btn_support);
        RelativeLayout likesRelley = findViewById(R.id.like_not_btn);
        RelativeLayout dislikesRelley = findViewById(R.id.dislike_not_btn);
        ImageView picture = findViewById(R.id.imgView_postPic);
        token = settings.getString("JWT","");
        personId = settings.getString("id", "");
        problemId = getIntent().getStringExtra("problem_id");
        likes = findViewById(R.id.likes);
        dislikes = findViewById(R.id.dislikes);
        imageLikes = findViewById(R.id.heart);
        imageDislikes = findViewById(R.id.broken_heart);
        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
        commentRecycler = findViewById(R.id.comment_recycler);
        EditText newComment = findViewById(R.id.post_comment);
        Button sendComment = findViewById(R.id.send_comment_button);

        myAssessmentRequest();




        GlideUrl glideUrl = new GlideUrl("https://netcrackeredu.herokuapp.com/downloadFile/" + pictureId, new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());

        if(pictureId != null) {
            Glide.with(this)
                    .load(glideUrl)
                    .apply(new RequestOptions().override(500, 500))
                    .into(picture);
        }


        likesRelley.setOnClickListener(view -> {
           if (pressedLikeBtn){
               imageLikes.setImageResource(R.drawable.heart);
               pressedLikeBtn = false;
               likeRequest(token, personId, problemId);
           } else {
               if (pressedDislikeBtn){
                   imageDislikes.setImageResource(R.drawable.broken);
                   pressedDislikeBtn = false;
               }
               imageLikes.setImageResource(R.drawable.heart_red);
               pressedLikeBtn = true;
               likeRequest(token, personId, problemId);
           }
        });

        dislikesRelley.setOnClickListener(view -> {
            if (pressedDislikeBtn){
                imageDislikes.setImageResource(R.drawable.broken);
                pressedDislikeBtn = false;
                dislikeRequest(token, personId, problemId);
                //обновляем текствью
            } else {
                if (pressedLikeBtn){
                    imageLikes.setImageResource(R.drawable.heart);
                    pressedLikeBtn = false;
                }

                imageDislikes.setImageResource(R.drawable.broken_black);
                pressedDislikeBtn =  true;
                dislikeRequest(token, personId, problemId);
            }
        });

        supportBtn.setOnClickListener(view -> ApplicationService.getInstance()
        .getJSONApi()
        .subscribe(token, problemId, personId)
        .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //showMessage("Подписался");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        }));


        sendComment.setOnClickListener(view -> {
            Comment comment = new Comment(new Person(personId), new Problem(problemId), newComment.getText().toString());
            ApplicationService.getInstance()
                    .getJSONApi()
                    .createComment(token, comment)
                    .enqueue(new Callback<CommentResponse>() {
                        @Override
                        public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                            showMessage("Комментарий успешно отправлен");
                            doRefresh();
                        }

                        @Override
                        public void onFailure(Call<CommentResponse> call, Throwable t) {

                        }
                    });

        });
        address.setText(getIntent().getStringExtra("problem_address"));
        type.setText(getIntent().getStringExtra("problem_type"));
        description.setText(getIntent().getStringExtra("problem_description"));
        date.setText(getIntent().getStringExtra("problem_date"));
        assessmentRequest(token, problemId);
        //dislikes.setText("dislikes: " + getIntent().getStringExtra("problem_dislikes"));

        String serverStatus = getIntent().getStringExtra("problem_status");

        switch (serverStatus) {
            case "created":
                status.setImageResource(R.drawable.red_circle);
                break;
            case "in_process":
                status.setImageResource(R.drawable.yellow_circle);
                break;
            case "solved":
                status.setImageResource(R.drawable.green_cirle);
                break;
        }

        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        adapter = new CommentPaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentRecycler.setLayoutManager(linearLayoutManager);
        commentRecycler.setItemAnimator(new DefaultItemAnimator());
        commentRecycler.setAdapter(adapter);

        commentRecycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    private void likeRequest(String token, String personId, String problemId){
        ApplicationService.getInstance()
                .getJSONApi()
                .setLike(token, problemId, personId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            //showMessage("Лайк поставлен/снят успешно");
                            assessmentRequest(token, problemId);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        //showMessage("Ошибка во время выполнения запроса: лайк");
                    }
                });
    }

    private void dislikeRequest(String token, String personId, String problemId){
        ApplicationService.getInstance()
                .getJSONApi()
                .setDislike(token, problemId, personId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            //запрос выполнился успешно
                            //showMessage("Дизайк поставлен/снят успешно");
                            assessmentRequest(token, problemId);
                        }
                        else {
                            //сервер вернул ошибку
                            //showMessage("Дизайк не поставлен, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: дизлайк");
                    }
                });
    }

    private void assessmentRequest(String token, String problemId){
        ApplicationService.getInstance()
                .getJSONApi()
                .getAssessment(token, problemId)
                .enqueue(new Callback<Assessment>() {
                    @Override
                    public void onResponse(@NonNull Call<Assessment> call, @NonNull Response<Assessment> response) {
                        if (response.isSuccessful()){
                            Assessment assessments = response.body();
                            //запрос выполнился успешно
                            likes.setText("likes: " + assessments.getLikesCount());
                            dislikes.setText("dislikes: " + assessments.getDislikesCount());
                            //showMessage("Оценки получены успешно");
                        }
                        else {
                            //сервер вернул ошибку
                            showMessage("Оценки не получены, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Assessment> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: оценка");
                    }
                });
    }

    private void myAssessmentRequest(){
        ApplicationService.getInstance()
                .getJSONApi()
                .getMyAssessment(token, problemId, personId)
                .enqueue(new Callback<MyAssessmentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MyAssessmentResponse> call, @NonNull Response<MyAssessmentResponse> response) {
                        if (response.isSuccessful()){
                            String myAssessment = response.body().getResponse();
                            showMessage(myAssessment);
                            //запрос выполнился успешно
                            if(myAssessment.equals("like")){
                                imageLikes.setImageResource(R.drawable.heart_red);
                                pressedLikeBtn = true;
                            }
                            else if(myAssessment.equals("dislike")){
                                imageDislikes.setImageResource(R.drawable.broken_black);
                                pressedDislikeBtn = true;
                            }
                        }
                        else {
                            //сервер вернул ошибку
                            showMessage("Оценки не получены, сервер вернул ошибку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<MyAssessmentResponse> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса: оценка");
                    }
                });
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
                break;
            case R.id.menu_settings:

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

        hideErrorView();
        currentPage = PAGE_START;

        callServerApi().enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                hideErrorView();

                List<Comment> results = fetchResults(response);
                //total_pages = response.body().getPagesLimit();
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<Comment> fetchResults(Response<CommentResponse> response) {
        List<Comment> feed2ProblemList = response.body().getCommentList();
        return feed2ProblemList;
    }

    private void loadNextPage() {

        callServerApi().enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<Comment> results = fetchResults(response);
                adapter.addAll(results);

                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private Call<CommentResponse> callServerApi() {
        return serverApi.getComments(
                token,
                problemId,
                currentPage,
                3,
                "creationDate"

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
