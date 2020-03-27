package com.milad.githubmvvmtest.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.Repository.ProjectRepository;

public class ProjectViewModel extends AndroidViewModel {

    private final LiveData<Project> projectObservable;
    private final LiveData<String> languageObservable;
    private final String userID;
    private final String projectID;

    public ObservableField<Project> project = new ObservableField<>();

    public ProjectViewModel(@NonNull Application application,final String userID , final String projectID) {
        super(application);
        this.userID = userID;
        this.projectID = projectID;

        projectObservable = ProjectRepository.getInstance().getProjectDetails(userID, projectID);
        languageObservable = ProjectRepository.getInstance().getProjectLanguages(userID, projectID);
    }

    public LiveData<Project> getObservableProject() {
        return projectObservable;
    }

    public LiveData<String> getObservableLanguage() {
        return languageObservable;
    }

    public void setProject(Project project) {
        this.project.set(project);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String userID;
        private final String projectID;

        public Factory(@NonNull Application application, String userID, String projectID) {
            this.application = application;
            this.userID = userID;
            this.projectID = projectID;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProjectViewModel(application, userID , projectID);
        }
    }
}
