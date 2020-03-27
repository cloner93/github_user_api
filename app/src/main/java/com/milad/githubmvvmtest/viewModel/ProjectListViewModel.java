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
    private final Single<List<Project>> projectListObservable;
    private final MutableLiveData<List<Project>> projectListLiveData = new MutableLiveData<List<Project>>() {
    };
    private final String userId;
    private Disposable disposableProject;

    public ProjectListViewModel(@NonNull Application application, String userId) {
        super(application);
        this.userId = userId;

        projectListObservable = ProjectRepository.getInstance().getProjectList(userId);
    }

    public LiveData<List<Project>> getProjectListObservable() {
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposableProject.isDisposed()) {
            disposableProject.dispose();
        }
    }
}
