package com.ilyarudyak.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mPlayer;

    public void stop() {
        if (isPlayerCreated()) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void pause() {
        if (isPlayerCreated())
            mPlayer.pause();
    }

    public void play(Context c) {

        if (!isPlayerCreated()) {

            mPlayer = MediaPlayer.create(c, R.raw.one_small_step);

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stop();
                }
            });
        }

        mPlayer.start();
    }

    public boolean isPlayerCreated() {
        return mPlayer != null;
    }

}