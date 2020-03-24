package com.example.problemsolver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.os.Bundle;

public class FeedActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(this, modelFeedArrayList);
        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();
    }
    //R.drawable.ic_propic1
    private void populateRecyclerView() {

        ModelFeed modelFeed = new ModelFeed(1, R.drawable.red_circle, 0, "30.08.17",  "Харченко, 16", "592", "Некоторое описание проблемы");
        modelFeedArrayList.add(modelFeed);

        modelFeed = new ModelFeed(2, R.drawable.green_circle, 0, "08.09.19", "Смольный буян, 18", "493", "Нет детских садов в округе");
        modelFeedArrayList.add(modelFeed);

        modelFeed = new ModelFeed(3, R.drawable.yellow_circle, 0, "21.03.20", "Воскресенская, 5", "124", "С супермаркетами беда");
        modelFeedArrayList.add(modelFeed);

        adapterFeed.notifyDataSetChanged();
    }
}




