package com.example.problemsolver.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.ProblemResultActivity;
import com.example.problemsolver.authorized.AuthorizedPerson;
import com.example.problemsolver.authorized.PersonArea;
import com.example.problemsolver.problemFeed.model.ProblemLastVisits;
import com.example.problemsolver.problemFeed.page.ProblemPageActivity;
import com.example.problemsolver.problemFeed.model.Feed2Problem;
import com.example.problemsolver.R;
import com.example.problemsolver.utils.PaginationAdapterCallback;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProblemPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Feed2Problem> problemsResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;
    private String personId;
    private String role;
    private String token;
    private SharedPreferences settings;

    private String statusInit = "init", statusCreated = "created", statusNotified = "notified",
            statusInProcess = "in_process", statusSolved = "solved",
            statusUnsolved = "unsolved", statusClosed = "closed";


    ProblemPaginationAdapter(Context context, String personId) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        problemsResults = new ArrayList<>();
        this.personId = personId;
        settings = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");
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
                View viewItem = inflater.inflate(R.layout.item_profile_feed, parent, false);
                viewHolder = new ProblemVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    void setStatusPic(Feed2Problem result, ProblemVH problemVH){
        if (result.getStatus().equals(statusCreated)){
            problemVH.mProblemImg.setImageResource(R.drawable.status_pic_unsolved);

        } else if (result.getStatus().equals(statusInProcess) || result.getStatus().equals(statusNotified)) {
            problemVH.mProblemImg.setImageResource(R.drawable.status_pic_in_progress);

        } else if (result.getStatus().equals(statusInit)) {
            problemVH.mProblemImg.setImageResource(R.drawable.status_pic_init);

        } else if (result.getStatus().equals(statusSolved)) {
            problemVH.mProblemImg.setImageResource(R.drawable.status_pic_solved);

        } else if (result.getStatus().equals(statusUnsolved) || result.getStatus().equals(statusClosed)) {
            problemVH.mProblemImg.setImageResource(R.drawable.status_pic_closed);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Feed2Problem result = problemsResults.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final ProblemVH problemVH = (ProblemVH) holder;
                if(result.getProblemLastVisits().stream().noneMatch( o -> o.getPerson().equals(personId))) {
                    problemVH.notification.setVisibility(View.VISIBLE);
                }
                else {
                    List<ProblemLastVisits> results = result.getProblemLastVisits().stream()
                            .filter( o -> o.getPerson().equals(personId))
                            .collect(Collectors.toList());
                    for (ProblemLastVisits res : results) {
                        if (Timestamp.valueOf(res.getLastVisitTime().replace("T"," ")).before(Timestamp.valueOf(result.getLastChangeTime().replace("T", " ")))) {
                            problemVH.notification.setVisibility(View.VISIBLE);
                        }
                    }

                }
                problemVH.mProblemTitle.setText(result.getAddress().toString());
                problemVH.mDate.setText(result.getCreationDate().substring(0, 10));
                problemVH.mProblemType.setText(result.getProblemName());
                problemVH.mRate.setText("Рейтинг: " + result.getRate().toString());

                setStatusPic(result, problemVH);

                role = settings.getString("Roles", "");
                if ("ROLE_SERVANT".equals(role)) {
                    problemVH.applyBtn.setText("Изменить статус");
                    if (!result.getStatus().equals("created") || !result.getStatus().equals("init")){
                        problemVH.applyBtn.setClickable(false);
                        problemVH.applyBtn.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                        problemVH.applyBtn.setTextColor(context.getResources().getColor(R.color.vinous));
                    }
                }

                problemVH.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), ProblemPageActivity.class);

                    intent.putExtra("problem_address",  result.getAddress().toString());
                    intent.putExtra("problem_type",  result.getProblemName());
                    intent.putExtra("problem_description", result.getDescription());
                    intent.putExtra("problem_date",  result.getCreationDate().substring(0, 10));
                    intent.putExtra("is_participant", result.getPersonsOfThisProblemAsParticipant().contains(personId));

                    //тут надо не рейтинг результата, а запрос лайки и дизлайки по id проблемы
                    intent.putExtra("problem_likes",  result.getRate().toString());
                    intent.putExtra("problem_status", result.getStatus());
                    intent.putExtra("problem_id", result.getId());
                    visitProblem(result.getId());
                    view.getContext().startActivity(intent);
                });

                problemVH.applyBtn.setOnClickListener(view -> {
                    if ("ROLE_SERVANT".equals(role)) {
                        changeProblemStatusToInProcess(result.getId());
                        problemVH.applyBtn.setClickable(false);
                        problemVH.applyBtn.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                        problemVH.applyBtn.setTextColor(context.getResources().getColor(R.color.vinous));

                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://letters.gov.spb.ru/reception/form/"));
                        context.startActivity(browserIntent);
                    }

                });

                problemVH.reportBtn.setOnClickListener(view -> {
                    Intent intent = new Intent(context, ProblemResultActivity.class);
                    intent.putExtra("problem_id", result.getId());
                    context.startActivity(intent);
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

    private void changeProblemStatusToInProcess(String problemId) {
        ApplicationService.getInstance()
                .getJSONApi()
                .putStatusByOrg(token, problemId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            showMessage("Статус проблемы был изменен на /в процессе/.");
                        }
                        else {
                            showMessage("Сервер вернул ошибку. Статус проблемы не был изменен.");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        showMessage("Ошибка во время выполнения запроса. Статус проблемы не был изменен.");
                    }
                });
    }

    private void visitProblem(String problemId) {
        ApplicationService.getInstance().getJSONApi()
                .visitProblem(token, problemId, personId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
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
        private Button applyBtn, reportBtn;
        private ImageView mProblemImg, notification;


        public ProblemVH(View itemView) {
            super(itemView);

            mProblemTitle = itemView.findViewById(R.id.street_name);
            mProblemType = itemView.findViewById(R.id.problem_description);
            mDate = itemView.findViewById(R.id.date);
            mRate = itemView.findViewById(R.id.rating);
            mProblemImg = itemView.findViewById(R.id.imgView_status_pic);
            applyBtn = itemView.findViewById(R.id.apply_btn);
            reportBtn = itemView.findViewById(R.id.report_button);
            notification = itemView.findViewById(R.id.imgView_notification);
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
