package com.ilyarudyak.android.sunset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SunsetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        getFragmentManager().beginTransaction()
                .add(android.R.id.content, SunsetFragment.newInstance())
                .commit();
    }
}
