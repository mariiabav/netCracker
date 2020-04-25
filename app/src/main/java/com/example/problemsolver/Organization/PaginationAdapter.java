package com.example.problemsolver.Organization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.Organization.model.RegisteredOrganization;
import com.example.problemsolver.R;
import com.example.problemsolver.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<RegisteredOrganization> eventResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    PaginationAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        eventResults = new ArrayList<>();
    }

    public List<RegisteredOrganization> getProblems() {
        return eventResults;
    }

    public void setProblems(List<RegisteredOrganization> eventResults) {
        this.eventResults = eventResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.event_feed, parent, false);
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
        RegisteredOrganization result = eventResults.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final ProblemVH problemVH = (ProblemVH) holder;

                problemVH.offerStatus.setText(result.getName());
                problemVH.offerDate.setText(result.getEmail());

                /*
                problemVH.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), ProblemPageActivity.class);

                    intent.putExtra("problem_address",  result.getProblem().getAddress().toString());
                    intent.putExtra("problem_type",  result.getProblem().getProblemName());
                    intent.putExtra("problem_description", result.getProblem().getDescription());
                    intent.putExtra("problem_date",  result.getProblem().getCreationDate().substring(0, 10));
                    intent.putExtra("problem_rating",  result.getProblem().getRate().toString());
                    intent.putExtra("problem_status", result.getProblem().getStatus());
                    intent.putExtra("problem_id", result.getId());
                    intent.putExtra("event_status", result.getOfferStatus());

                    view.getContext().startActivity(intent);
                });

                 */



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
        return eventResults == null ? 0 : eventResults.size();
    }

    @Override
    public int getItemViewType(int position) {
            return (position == eventResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private void add(RegisteredOrganization r) {
        eventResults.add(r);
        notifyItemInserted(eventResults.size() - 1);
    }

    public void addAll(List<RegisteredOrganization> moveResults) {
        for (RegisteredOrganization result : moveResults) {
            add(result);
        }
    }

    private void remove(RegisteredOrganization r) {
        int position = eventResults.indexOf(r);
        if (position > -1) {
            eventResults.remove(position);
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
        add(new RegisteredOrganization());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = eventResults.size() - 1;
        RegisteredOrganization result = getItem(position);

        if (result != null) {
            eventResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RegisteredOrganization getItem(int position) {
        return eventResults.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(eventResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    protected class ProblemVH extends RecyclerView.ViewHolder {
        private TextView offerStatus;
        private TextView offerDate;

        private ProgressBar mProgress;

        public ProblemVH(View itemView) {
            super(itemView);

            offerStatus = itemView.findViewById(R.id.event_status);
            offerDate = itemView.findViewById(R.id.date);
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
