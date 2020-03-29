package com.milad.githubmvvmtest.model.Repository;

import android.app.Application;
import android.content.SharedPreferences;

public class StoreUserName {
    private Application application;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public StoreUserName(Application application) {
        this.application = application;

        sharedpreferences = application.getSharedPreferences("storeUserName", application.MODE_PRIVATE);

        editor = sharedpreferences.edit();
    }

    public String getUserName() {
        return (sharedpreferences.getString("userName", ""));
    }

    public void setUserName(String userID) {
        editor.putString("userName", userID);
        editor.commit();
    }
}
