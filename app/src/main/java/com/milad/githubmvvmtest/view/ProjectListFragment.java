package com.milad.githubmvvmtest.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.databinding.FragmentProjectListBinding;
import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.view.adapter.ProjectListAdapter;
import com.milad.githubmvvmtest.view.callback.ProjectClickCallback;
import com.milad.githubmvvmtest.viewModel.ProjectListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectListFragment extends Fragment {

    static final String TAG = "ProjectListFragment";
    private static final String KEY_USER_ID = "userId";
    private ProjectListAdapter projectListAdapter;
    private FragmentProjectListBinding binding;

    public ProjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_project_list, container, false);

        projectListAdapter = new ProjectListAdapter(projectClickCallback);
        binding.projectList.setAdapter(projectListAdapter);
        binding.setIsLoading(true);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ProjectListViewModel.Factory factory = new ProjectListViewModel.Factory(
                getActivity().getApplication());
        final ProjectListViewModel viewModel =
                ViewModelProviders.of(this, factory).get(ProjectListViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(final ProjectListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProjectListObservable().observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                if (projects != null) {
                    binding.setIsLoading(false);
                    binding.setUserId(viewModel.getUserId());
                    projectListAdapter.setProjectList(projects);
                }
            }
        });
    }

    public static ProjectListFragment forProject() {
        return new ProjectListFragment();
    }

    private final ProjectClickCallback projectClickCallback = new ProjectClickCallback() {

        @Override
        public void onClick(Project project) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).showProjectDetail(project);
            }
        }
    };
}
