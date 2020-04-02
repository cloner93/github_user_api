package com.milad.githubmvvmtest.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.databinding.MainFragmentBinding;
import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.Repository.StoreUserName;
import com.milad.githubmvvmtest.model.User;
import com.milad.githubmvvmtest.view.adapter.MainProjectListAdapter;
import com.milad.githubmvvmtest.view.callback.MainFragmentClickCallback;
import com.milad.githubmvvmtest.view.callback.ProjectClickCallback;
import com.milad.githubmvvmtest.viewModel.MainFragmentViewModel;

import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements View.OnClickListener, BottomSheetFragment.onBottomsheetNameIdListener {

    static final String TAG = "jojo";
    private MainFragmentBinding binding;
    private MainProjectListAdapter mainProjectListAdapter;
    MainFragmentViewModel viewModel;

    public MainFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        binding.setOnClickHandler(callback);

        mainProjectListAdapter = new MainProjectListAdapter(projectClickCallback);
        binding.projectListMain.setAdapter(mainProjectListAdapter);
        binding.imageView6.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainFragmentViewModel.Factory factory = new MainFragmentViewModel.Factory(getActivity().getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(MainFragmentViewModel.class);

        userIDViewModel();
    }

    private void userIDViewModel() {
        String userId = viewModel.getUserId();
        if (userId.equals(""))
            initBottomSheet();
        else {
            userInfoObserveViewModel();
            projectObserveViewModel();
        }
    }

    private void userInfoObserveViewModel() {
        viewModel.getUserInfoObservable().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    binding.setUser(user);
                }
            }
        });
    }

    private void projectObserveViewModel() {
        viewModel.getProjectListObservable().observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
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
            ((MainActivity) Objects.requireNonNull(getContext())).showProjectList();
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
                ((MainActivity) getActivity()).showProjectDetail(project);
            }
        }
    };

    @Override
    public void onClick(View v) {
        initBottomSheet();
    }

    private void initBottomSheet() {
        BottomSheetFragment bottomSheetDialog = BottomSheetFragment.newInstance();
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom_Sheet");
        bottomSheetDialog.setOnBottomsheetNameIdListener(this);
    }

    @Override
    public void setOnNameId(String nameID) {
        viewModel.setUserId(nameID);

        userInfoObserveViewModel();
        projectObserveViewModel();
    }
}
