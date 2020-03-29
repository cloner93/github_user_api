package com.milad.githubmvvmtest.model.Repository;

import android.app.Application;
import android.util.Log;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public class ProjectRepository {
    private StoreUserName sharedPrefService = null;
    private GitHubService gitHubService = null;
    private static ProjectRepository projectRepository;
    private String userId;

    private ProjectRepository(Application application, String userId) {
        Log.d("jojo", "ProjectRepository -> ProjectRepository: init in constrictor");
        gitHubService = new getGitHubService().getRetrofit();
        sharedPrefService = new StoreUserName(application);
        this.userId = userId;
    }

    public synchronized static ProjectRepository getInstance(Application application, String userID) {
        if (projectRepository == null) {
            projectRepository = new ProjectRepository(application, userID);
        }
        return projectRepository;
    }

    //<editor-fold desc="shared pref">
    public Single<String> getUserID() {
        return sharedPrefService.getUserName();
    }

    public void setUserID(String userID) {
        sharedPrefService.setUserName(userID);
    }
    //</editor-fold>

    //<editor-fold desc="Github service">
    public Single<User> getUser() {
        return gitHubService.getUser(userId);
    }

    public Single<List<Project>> getProjectList() {
        return gitHubService.getProjectList(userId);
    }

    public Single<Project> getProjectDetails(String projectName) {
        return gitHubService.getProjectDetails(userId, projectName);
    }

    public Single<ResponseBody> getProjectLanguages(String projectName) {

        return gitHubService.getProjectLanguages(userId, projectName);
    }
    //</editor-fold>

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}