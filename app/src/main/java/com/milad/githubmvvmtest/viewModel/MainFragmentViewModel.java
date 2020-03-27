package com.milad.githubmvvmtest.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.Repository.ProjectRepository;
import com.milad.githubmvvmtest.model.User;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentViewModel extends AndroidViewModel {
    private final Single<User> userInfoObservable;
    private final Single<List<Project>> projectListObservable;

    private final MutableLiveData<User> userInfoLiveData = new MutableLiveData<User>() {
    };
    private final MutableLiveData<List<Project>> projectListLiveData = new MediatorLiveData<List<Project>>() {
    };

    private Disposable disposableUserInfo;
    private Disposable disposableProject;

    private final String userId;

    public MainFragmentViewModel(@NonNull Application application, final String userId) {
        super(application);
        this.userId = userId;

        userInfoObservable = ProjectRepository.getInstance().getUser(userId);
        projectListObservable = ProjectRepository.getInstance().getProjectList(userId);
    }

    public LiveData<User> getUserInfoObservable() {
        userInfoObservable.subscribeOn(Schedulers.io()).subscribe(new SingleObserver<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableUserInfo = d;
            }

            @Override
            public void onSuccess(User user) {
                userInfoLiveData.setValue(user);
            }

            @Override
            public void onError(Throwable e) {
                userInfoLiveData.setValue(null);
            }
        });
        return userInfoLiveData;
    }

    public LiveData<List<Project>> getProjectListObservable() {
        projectListObservable.subscribeOn(Schedulers.io()).subscribe(new SingleObserver<List<Project>>() {
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
                projectListLiveData.setValue(null);
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
            return (T) new MainFragmentViewModel(application, userId);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposableProject.isDisposed())
            disposableProject.dispose();
        if (!disposableUserInfo.isDisposed()) {
            disposableUserInfo.dispose();
        }
    }
}
