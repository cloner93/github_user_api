package com.milad.githubmvvmtest.viewModel;

import android.app.Application;
import android.util.Log;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentViewModel extends AndroidViewModel {
    private final MutableLiveData<User> userInfoLiveData = new MutableLiveData<User>() {
    };
    private final MutableLiveData<List<Project>> projectListLiveData = new MediatorLiveData<List<Project>>() {
    };

    private CompositeDisposable disposable = new CompositeDisposable();

    private ProjectRepository repository;

    private String userId;

    public MainFragmentViewModel(@NonNull Application application) {
        super(application);

        repository = ProjectRepository.getInstance(application);

        userId = repository.getUserID();
    }

    public void setUserId(String userID) {
        repository.setUserID(userID);
        userId = userID;
    }

    public String getUserId() {
        return repository.getUserID();
    }

    public LiveData<User> getUserInfoObservable() {
        Single<User> userInfoObservable = repository.getUser(userId);

        userInfoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        userInfoLiveData.setValue(user);
                        Log.d("jojo", "getUserInfoObservable: " + user.login);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("jojo", "getUserInfoObservable: " + e.toString());

                        userInfoLiveData.setValue(null);
                    }
                });
        return userInfoLiveData;
    }

    public LiveData<List<Project>> getProjectListObservable() {
        Single<List<Project>> projectListObservable = repository.getProjectList(userId);
        projectListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Project>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
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

        public Factory(@NonNull Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MainFragmentViewModel(application);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
