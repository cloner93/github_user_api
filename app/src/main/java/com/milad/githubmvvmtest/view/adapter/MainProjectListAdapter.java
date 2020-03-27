package com.milad.githubmvvmtest.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.databinding.MainProjectListItemBinding;
import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.view.callback.ProjectClickCallback;

import java.util.List;
import java.util.Objects;

public class MainProjectListAdapter extends RecyclerView.Adapter<MainProjectListAdapter.MainProjectViewHolder> {

    private List<? extends Project> projectList;
    MainProjectListItemBinding mBinding;
    Context context;

    @Nullable
    private final ProjectClickCallback projectClickCallback;

    public MainProjectListAdapter(@Nullable ProjectClickCallback projectClickCallback) {
        this.projectClickCallback = projectClickCallback;
    }
    public void setProjectList(final List<? extends Project> projectList){
        if (this.projectList == null) {
            this.projectList = projectList;
            notifyItemRangeInserted(0, projectList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MainProjectListAdapter.this.projectList.size();
                }

                @Override
                public int getNewListSize() {
                    return projectList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MainProjectListAdapter.this.projectList.get(oldItemPosition).id ==
                            projectList.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Project project = projectList.get(newItemPosition);
                    Project old = projectList.get(oldItemPosition);
                    return project.id == old.id
                            && Objects.equals(project.git_url, old.git_url);
                }
            });
            this.projectList = projectList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public MainProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.main_project_list_item, parent, false);

        this.context = parent.getContext();
        mBinding.setCallback(projectClickCallback);

        return new MainProjectListAdapter.MainProjectViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainProjectViewHolder holder, int position) {
        holder.binding.setProject(projectList.get(position));
        try {
            holder.binding.executePendingBindings();
            Log.v("data" , "in try");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("data" , "out try \n"+ e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
    }

    static class MainProjectViewHolder extends RecyclerView.ViewHolder {

        final MainProjectListItemBinding binding;

        public MainProjectViewHolder(MainProjectListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
