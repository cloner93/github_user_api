package com.milad.githubmvvmtest.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.Repository.ProjectRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Project>> projectListLiveData = new MutableLiveData<List<Project>>() {
    };
    private final String userId;
    private ProjectRepository repository;

    private Disposable disposableProject;

    public ProjectListViewModel(@NonNull Application application) {
        super(application);

        repository = ProjectRepository.getInstance(application);
        userId = repository.getUserID();
    }

    public String getUserId() {
        return repository.getUserID();
    }

    public LiveData<List<Project>> getProjectListObservable() {
        Single<List<Project>> projectListObservable = repository.getProjectList(userId);

        projectListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Project>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableProject = d;
                    }

                    @Override
                    public void onSuccess(List<Project> projects) {
                        projectListLiveData.setValue(projects);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        return projectListLiveData;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        public Factory(@NonNull Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProjectListViewModel(application);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposableProject.isDisposed()) {
            disposableProject.dispose();
        }
    }
}
