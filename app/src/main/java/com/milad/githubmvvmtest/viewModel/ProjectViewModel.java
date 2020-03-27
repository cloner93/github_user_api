package com.milad.githubmvvmtest.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.Repository.ProjectRepository;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ProjectViewModel extends AndroidViewModel {

    private final Single<Project> projectObservable;
    private final Single<ResponseBody> languageObservable;

    private final MutableLiveData<Project> projectLiveData = new MutableLiveData<Project>() {
    };
    private final MutableLiveData<String> languageLiveData = new MutableLiveData<String>() {
    };

    private final String userID;
    private final String projectID;

    private Disposable disposableProject;
    private Disposable disposableLanguage;

    public ObservableField<Project> project = new ObservableField<>();

    public ProjectViewModel(@NonNull Application application, final String userID, final String projectID) {
        super(application);
        this.userID = userID;
        this.projectID = projectID;

        projectObservable = ProjectRepository.getInstance().getProjectDetails(userID, projectID);
        languageObservable = ProjectRepository.getInstance().getProjectLanguages(userID, projectID);
    }


    public LiveData<Project> getObservableProject() {
        projectObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Project>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableProject = d;
                    }

                    @Override
                    public void onSuccess(Project project) {
                        projectLiveData.setValue(project);
                    }

                    @Override
                    public void onError(Throwable e) {
                        projectLiveData.setValue(null);
                    }
                });

        return projectLiveData;
    }

    public LiveData<String> getObservableLanguage() {
        languageObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableLanguage = d;
                    }

                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        Log.d("esh", "onSuccess: " + responseBody.toString());
                        languageLiveData.setValue(responseBody.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        languageLiveData.setValue("");
                    }
                });
        return languageLiveData;
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
            return (T) new ProjectViewModel(application, userID, projectID);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposableLanguage.isDisposed()) {
            disposableLanguage.dispose();
        }
        if (!disposableProject.isDisposed()) {
            disposableProject.dispose();
        }
    }
}
