package com.example.problemsolver.Feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Feed.AdapterFeed;
import com.example.problemsolver.Feed.FeedProblem;
import com.example.problemsolver.R;
import com.example.problemsolver.Registration.RegisteredPerson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    ArrayList<FeedProblem> feedProblemArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(this, feedProblemArrayList);
        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();
    }

    private void populateRecyclerView() {


        ApplicationService.getInstance()
                .getJSONApi()
                .getAllProblems()
                .enqueue(new Callback<List<Feed2Problem>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Feed2Problem>> call, @NonNull Response<List<Feed2Problem>> response) {
                        if (response.isSuccessful()){
                            ArrayList<Feed2Problem> allProblems = (ArrayList<Feed2Problem>) response.body();

                            for (Feed2Problem problem : allProblems){
                                FeedProblem feedProblem = new FeedProblem(problem.getCreationDate(),
                                        problem.getAddress().getStreet(), problem.getRate().toString(), problem.getDescription());

                                feedProblemArrayList.add(feedProblem);
                            }
                        }
                        else {
                            showMessage("Проблемы не получены");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Feed2Problem>> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса");
                    }
                });
        FeedProblem feedProblem = new FeedProblem("30.08.17",  "Харченко, 16", "592", "Некоторое описание проблемы");
        feedProblemArrayList.add(feedProblem);

        feedProblem = new FeedProblem("08.09.19", "Смольный буян, 18", "493", "Нет детских садов в округе");
        feedProblemArrayList.add(feedProblem);

        feedProblem = new FeedProblem("21.03.20", "Воскресенская, 5", "124", "С супермаркетами беда");
        feedProblemArrayList.add(feedProblem);

    }

    private void showMessage(String string){
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}
