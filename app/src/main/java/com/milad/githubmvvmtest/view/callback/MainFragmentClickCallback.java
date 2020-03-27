package com.milad.githubmvvmtest.view.callback;

import android.view.View;

public interface MainFragmentClickCallback {

    void onGetProjects(String userId);

    void onGetEvents( String userId);
}
