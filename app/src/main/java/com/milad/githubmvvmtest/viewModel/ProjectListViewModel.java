package com.milad.githubmvvmtest.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.Repository.ProjectRepository;

import java.util.List;

public class ProjectListViewModel extends AndroidViewModel {
    private final LiveData<List<Project>> projectListObservable;
    private final String userId;

    public ProjectListViewModel(@NonNull Application application, String userId) {
        super(application);
        this.userId= userId;

        projectListObservable = ProjectRepository.getInstance().getProjectList(userId);
    }

    public LiveData<List<Project>> getProjectListObservable(){
        return projectListObservable;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String userId;

        public Factory(@NonNull Application application, String userId) {
            this.application = application;
            this.userId = userId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProjectListViewModel(application, userId);
        }
    }
}
