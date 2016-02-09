package com.ilyarudyak.android.draganddraw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        getFragmentManager().beginTransaction()
                .add(android.R.id.content, MainFragment.newInstance())
                .commit();
    }
}
