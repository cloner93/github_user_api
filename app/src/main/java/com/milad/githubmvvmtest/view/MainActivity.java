package com.milad.githubmvvmtest.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.milad.githubmvvmtest.R;
import com.milad.githubmvvmtest.model.Project;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            MainFragment fragment = new MainFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, MainFragment.TAG)
                    .commit();
        }
    }

    /**
     * Shows the project detail fragment
     */
    public void showProjectDetail(String userId, Project project) {
        ProjectFragment projectFragment = ProjectFragment.forProject(userId, project.name);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("project")
                .replace(R.id.fragment_container, projectFragment, null).commit();
    }

    public void showProjectList(String userId) {
        ProjectListFragment fragment = ProjectListFragment.forProject(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("list project")
                .replace(R.id.fragment_container, fragment, null).commit();
    }
}