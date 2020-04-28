package com.example.problemsolver.problemFeed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.problemFeed.Page.ProblemPageActivity;
import com.example.problemsolver.problemFeed.model.Feed2Problem;
import com.example.problemsolver.R;
import com.example.problemsolver.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ProblemPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Feed2Problem> problemsResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    ProblemPaginationAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        problemsResults = new ArrayList<>();
    }

    public List<Feed2Problem> getProblems() {
        return problemsResults;
    }

    public void setProblems(List<Feed2Problem> movieResults) {
        this.problemsResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new ProblemVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Feed2Problem result = problemsResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final ProblemVH problemVH = (ProblemVH) holder;

                problemVH.mProblemTitle.setText(result.getAddress().toString());
                problemVH.mDate.setText(result.getCreationDate().substring(0, 10));
                problemVH.mProblemType.setText(result.getProblemName());
                problemVH.mRate.setText("Рейтинг: " + result.getRate().toString());

                problemVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ProblemPageActivity.class);

                        intent.putExtra("problem_address",  result.getAddress().toString());
                        intent.putExtra("problem_type",  result.getProblemName());
                        intent.putExtra("problem_description", result.getDescription());
                        intent.putExtra("problem_date",  result.getCreationDate().substring(0, 10));

                        //тут надо не рейтинг результата, а запрос лайки и дизлайки по id проблемы
                        intent.putExtra("problem_likes",  result.getRate().toString());
                        if(result.getPicture() != null) {
                            intent.putExtra("picture_id", result.getPicture().getId());
                        }


                        intent.putExtra("problem_status", result.getStatus());
                        intent.putExtra("problem_id", result.getId());

                        view.getContext().startActivity(intent);
                    }
                });


                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void showMessage(String string){
        Toast t = Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public int getItemCount() {
        return problemsResults == null ? 0 : problemsResults.size();
    }

    @Override
    public int getItemViewType(int position) {
            return (position == problemsResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private void add(Feed2Problem r) {
        problemsResults.add(r);
        notifyItemInserted(problemsResults.size() - 1);
    }

    public void addAll(List<Feed2Problem> moveResults) {
        for (Feed2Problem result : moveResults) {
            add(result);
        }
    }

    private void remove(Feed2Problem r) {
        int position = problemsResults.indexOf(r);
        if (position > -1) {
            problemsResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Feed2Problem());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = problemsResults.size() - 1;
        Feed2Problem result = getItem(position);

        if (result != null) {
            problemsResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Feed2Problem getItem(int position) {
        return problemsResults.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(problemsResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    protected class ProblemVH extends RecyclerView.ViewHolder {
        private TextView mProblemTitle;
        private TextView mProblemType;
        private TextView mDate;
        private TextView mRate;
        private ImageView mProblemImg;

        private ProgressBar mProgress;

        public ProblemVH(View itemView) {
            super(itemView);

            mProblemTitle = itemView.findViewById(R.id.street_name);
            mProblemType = itemView.findViewById(R.id.problem_descriprion);
            mDate = itemView.findViewById(R.id.date);
            mRate = itemView.findViewById(R.id.rating);
            mProblemImg = itemView.findViewById(R.id.status_pic);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

}
