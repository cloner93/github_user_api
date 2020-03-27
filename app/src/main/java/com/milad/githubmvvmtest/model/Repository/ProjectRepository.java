package com.milad.githubmvvmtest.model.Repository;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectRepository {
    private GitHubService gitHubService;
    private static ProjectRepository projectRepository;

    private ProjectRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitHubService.HTTPS_API_GITHUB_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubService = retrofit.create(GitHubService.class);
    }

    public synchronized static ProjectRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new ProjectRepository();
        }
        return projectRepository;
    }

    public Single<User> getUser(String userId) {
        return gitHubService.getUser(userId);
    }

    public Single<List<Project>> getProjectList(String userId) {
        return gitHubService.getProjectList(userId);
    }

    public Single<Project> getProjectDetails(String userID, String projectName) {
        return gitHubService.getProjectDetails(userID, projectName);
    }

    public Single<ResponseBody> getProjectLanguages(String userID, String projectName) {

        return gitHubService.getProjectLanguages(userID, projectName);
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}