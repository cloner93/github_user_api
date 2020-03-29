package com.milad.githubmvvmtest.model.Repository;

import android.app.Application;
import android.content.SharedPreferences;

import io.reactivex.Single;

public class StoreUserName {
    private Application application;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public StoreUserName(Application application) {
        this.application = application;

        sharedpreferences = application.getSharedPreferences("storeUserName", application.MODE_PRIVATE);

        editor = sharedpreferences.edit();
    }

    public Single<String> getUserName() {
        return Single.just(sharedpreferences.getString("userName", ""));
    }

    public void setUserName(String userID) {
        editor.putString("userName", userID);
        editor.commit();
    }
}
