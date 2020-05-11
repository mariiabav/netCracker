package com.example.problemsolver.problemFeed.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.problemFeed.model.Comment;
import com.example.problemsolver.R;
import com.example.problemsolver.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CommentPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Comment> problemsResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    CommentPaginationAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        problemsResults = new ArrayList<>();
    }

    public List<Comment> getProblems() {
        return problemsResults;
    }

    public void setProblems(List<Comment> movieResults) {
        this.problemsResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_comment, parent, false);
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
        Comment result = problemsResults.get(position);

        switch (getItemViewType(position)) {

            case ITEM:
                final ProblemVH problemVH = (ProblemVH) holder;
                problemVH.person.setText(result.getPerson().getFirstName() + " " + result.getPerson().getSecondName());
                problemVH.text.setText(result.getText());
                problemVH.creationDate.setText(result.getCreationDate().substring(0, 10));

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

    private void add(Comment r) {
        problemsResults.add(r);
        notifyItemInserted(problemsResults.size() - 1);
    }

    public void addAll(List<Comment> moveResults) {
        for (Comment result : moveResults) {
            add(result);
        }
    }

    private void remove(Comment r) {
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
        add(new Comment());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = problemsResults.size() - 1;
        Comment result = getItem(position);

        if (result != null) {
            problemsResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Comment getItem(int position) {
        return problemsResults.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(problemsResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    protected class ProblemVH extends RecyclerView.ViewHolder {
        private TextView text;
        private TextView person;
        private TextView creationDate;

        private ProgressBar mProgress;

        public ProblemVH(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.comment_content);
            person = itemView.findViewById(R.id.comment_username);
            creationDate = itemView.findViewById(R.id.comment_date);
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

