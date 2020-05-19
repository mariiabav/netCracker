package com.example.problemsolver.event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.example.problemsolver.R;
import com.example.problemsolver.utils.PaginationAdapterCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class OrgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String sortBy;

    private List<RegisteredOrganization> orgResult;
    private Context context;

    private String errorMsg, orgId;

    public OrgAdapter(Context context) {
        this.context = context;
        orgResult = new ArrayList<>();
    }

    public List<RegisteredOrganization> getOrgs() {
        return orgResult;
    }

    public void setOrgs(List<RegisteredOrganization> orgResult) {
        this.orgResult = orgResult;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View viewItem = inflater.inflate(R.layout.item_organization_to_choose, parent, false);
        viewHolder = new OrganizationVH(viewItem);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RegisteredOrganization result = orgResult.get(position);


        final OrganizationVH organizationVH = (OrganizationVH) holder;

        organizationVH.orgName.setText(result.getName());
        organizationVH.address.setText(result.getAddress().toString());
        organizationVH.email.setText(result.getEmail());
        organizationVH.phone.setText(result.getPhone());

        organizationVH.rate.setText(result.getRate());
        organizationVH.chooseBtn.setTag(result.getId().toString());


    }


    @Override
    public int getItemCount() {
        return orgResult == null ? 0 : orgResult.size();
    }

    private void add(RegisteredOrganization r) {
        orgResult.add(r);
        notifyItemInserted(orgResult.size() - 1);
    }

    public void addAll(List<RegisteredOrganization> moveResults) {
        for (RegisteredOrganization result : moveResults) {
            add(result);
        }
    }

    public void addSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    private void remove(RegisteredOrganization r) {
        int position = orgResult.indexOf(r);
        if (position > -1) {
            orgResult.remove(position);
            notifyItemRemoved(position);
        }
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public RegisteredOrganization getItem(int position) {
        return orgResult.get(position);
    }


    protected class OrganizationVH extends RecyclerView.ViewHolder {
        private TextView orgName;
        private TextView address;
        private TextView email;
        private TextView phone;
        private TextView rate;
        private Button chooseBtn;

        OrganizationVH(View itemView) {
            super(itemView);

            orgName = itemView.findViewById(R.id.org_name);
            address = itemView.findViewById(R.id.org_address);
            email = itemView.findViewById(R.id.org_email);
            phone = itemView.findViewById(R.id.org_phone);
            rate = itemView.findViewById(R.id.org_rate);
            chooseBtn = itemView.findViewById(R.id.chOrgBtn);
        }
    }
}


