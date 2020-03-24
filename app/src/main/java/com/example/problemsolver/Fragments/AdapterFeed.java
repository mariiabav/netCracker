package com.example.problemsolver.Fragments;

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
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    RequestManager glide;

    public AdapterFeed(Context context, ArrayList<ModelFeed> modelFeedArrayList) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
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
        final ModelFeed modelFeed = modelFeedArrayList.get(position);

        holder.street_name.setText(modelFeed.getStreet());
        holder.date.setText(modelFeed.getDate());
        holder.rating.setText("Рейтинг: " + modelFeed.getRating());
        holder.description.setText(String.valueOf(modelFeed.getDescription()));

        glide.load(modelFeed.getStatusPic()).into(holder.status_pic);


        if (modelFeed.getPostPic() == 0) {
            holder.problem_pic.setVisibility(View.GONE);
        } else {
            holder.problem_pic.setVisibility(View.VISIBLE);
            glide.load(modelFeed.getPostPic()).into(holder.problem_pic);
        }


    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, street_name, rating, description;
        ImageView status_pic, problem_pic;


        public MyViewHolder(View itemView) {
            super(itemView);

            status_pic = (ImageView) itemView.findViewById(R.id.status_pic);
            problem_pic = (ImageView) itemView.findViewById(R.id.imgView_postPic);

            date = (TextView) itemView.findViewById(R.id.date);
            street_name = (TextView) itemView.findViewById(R.id.street_name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            description = (TextView) itemView.findViewById(R.id.problem_descriprion);
        }
    }
}
