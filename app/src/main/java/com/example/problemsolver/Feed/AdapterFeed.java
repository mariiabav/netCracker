package com.example.problemsolver.Feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.example.problemsolver.R;

import java.util.ArrayList;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    Context context;
    ArrayList<FeedProblem> feedProblemArrayList = new ArrayList<>();
    RequestManager glide;

    public AdapterFeed(Context context, ArrayList<FeedProblem> feedProblemArrayList) {

        this.context = context;
        this.feedProblemArrayList = feedProblemArrayList;
        glide = Glide.with(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final FeedProblem feedProblem = feedProblemArrayList.get(position);


        holder.street_name.setText(feedProblem.getStreet());
        holder.date.setText(feedProblem.getDate());
        holder.rating.setText("Рейтинг: " + feedProblem.getRating());
        holder.description.setText(String.valueOf(feedProblem.getDescription()));

        glide.load(feedProblem.getStatusPic()).into(holder.status_pic);


    }

    @Override
    public int getItemCount() {
        return feedProblemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, street_name, rating, description;
        ImageView status_pic, pic;

        public MyViewHolder(View itemView) {
            super(itemView);

            status_pic = itemView.findViewById(R.id.status_pic);

            date = itemView.findViewById(R.id.date);
            street_name = itemView.findViewById(R.id.street_name);
            rating = itemView.findViewById(R.id.rating);
            description = itemView.findViewById(R.id.problem_descriprion);
        }
    }
}
