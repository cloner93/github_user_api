package com.milad.githubmvvmtest.model.Repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class StoreUserName {
    private Context application;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public StoreUserName(Context application) {
        this.application = application;

        sharedpreferences = application.getSharedPreferences("storeUserName", application.MODE_PRIVATE);

        editor = sharedpreferences.edit();
    }

    public String getUserName() {
        return sharedpreferences.getString("userName", "");
    }

    public void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.commit();
    }
}
