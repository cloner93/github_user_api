package com.milad.githubmvvmtest.model.Repository;

import com.milad.githubmvvmtest.model.Project;
import com.milad.githubmvvmtest.model.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    String HTTPS_API_GITHUB_URL = "https://api.github.com/";

    @GET("users/{user}")
    Single<User> getUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Single<List<Project>> getProjectList(@Path("user") String user);

    @GET("/repos/{user}/{reponame}")
    Single<Project> getProjectDetails(@Path("user") String user, @Path("reponame") String projectName);

    @GET("/repos/{user}/{reponame}/languages")
    Single<ResponseBody> getProjectLanguages(@Path("user") String user, @Path("reponame") String projectName);
}
