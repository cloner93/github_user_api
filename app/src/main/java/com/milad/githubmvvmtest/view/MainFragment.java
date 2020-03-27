package com.milad.githubmvvmtest.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.databinding.MainFragmentBinding;
import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.User;
import com.milad.githubmvvmtest.view.adapter.MainProjectListAdapter;
import com.milad.githubmvvmtest.view.callback.MainFragmentClickCallback;
import com.milad.githubmvvmtest.view.callback.ProjectClickCallback;
import com.milad.githubmvvmtest.viewModel.MainFragmentViewModel;

import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {

    static final String TAG = "MainFragment";
    private MainFragmentBinding binding;
    private MainProjectListAdapter mainProjectListAdapter;

    private String userId = "google";

    public MainFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        binding.setOnClickHandler(callback);

        mainProjectListAdapter = new MainProjectListAdapter(projectClickCallback);
        binding.projectListMain.setAdapter(mainProjectListAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainFragmentViewModel.Factory factory = new MainFragmentViewModel.Factory(
                getActivity().getApplication(), userId);

        final MainFragmentViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(MainFragmentViewModel.class);

        userInfoObserveViewModel(viewModel);
        projectObserveViewModel(viewModel);
    }

    private void userInfoObserveViewModel(MainFragmentViewModel viewModel) {
        viewModel.getUserInfoObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    binding.setUser(user);
                }
            }
        });
    }

    private void projectObserveViewModel(MainFragmentViewModel viewModel) {
        viewModel.getProjectListObservable().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                if (projects != null) {
                    //TODO: واکشی پروژه ها برای صفخه اصلی
                    mainProjectListAdapter.setProjectList(projects);
                }
            }
        });
    }

    private final MainFragmentClickCallback callback = new MainFragmentClickCallback() {
        @Override
        public void onGetProjects(String userId) {
            ((MainActivity) Objects.requireNonNull(getContext())).showProjectList(userId);
        }

        @Override
        public void onGetEvents(String userId) {
            Toast.makeText(getContext(), userId, Toast.LENGTH_SHORT).show();
            //new jsonUtils().getColor(getContext(), "Mercury");
        }
    };

    private final ProjectClickCallback projectClickCallback = new ProjectClickCallback() {

        @Override
        public void onClick(Project project) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).showProjectDetail(userId ,project);
            }
        }
    };
}
