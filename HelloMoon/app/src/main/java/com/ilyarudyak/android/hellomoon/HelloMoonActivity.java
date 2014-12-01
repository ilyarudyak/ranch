package com.ilyarudyak.android.hellomoon;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HelloMoonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_moon);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_moon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Button mPlayButton;
        private Button mStopButton;
        private Button mPauseButton;

        private AudioPlayer mPlayer = new AudioPlayer();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hello_moon, container, false);

            mPlayButton = (Button) rootView.findViewById(R.id.hellomoon_playButton);
            mStopButton = (Button) rootView.findViewById(R.id.hellomoon_stopButton);
            mPauseButton = (Button) rootView.findViewById(R.id.hellomoon_pauseButton);

            mPlayButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mPlayer.play(getActivity());
                    updateButtons();
                }
            });

            mPauseButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mPlayer.pause();
                    mPlayButton.setEnabled(true);
                }
            });

            mStopButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mPlayer.stop();
                    updateButtons();
                }
            });

            return rootView;
        }

        private void updateButtons() {
            boolean isEnabled = !mPlayer.isPlayerCreated();
            mPlayButton.setEnabled(isEnabled);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mPlayer.stop();
        }
    }
}
