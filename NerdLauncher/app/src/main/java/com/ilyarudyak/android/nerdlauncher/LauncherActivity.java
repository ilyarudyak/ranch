package com.ilyarudyak.android.nerdlauncher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (savedInstanceState != null) {
            return;
        }

        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, LauncherFragment.newInstance())
                .commit();
    }
}
