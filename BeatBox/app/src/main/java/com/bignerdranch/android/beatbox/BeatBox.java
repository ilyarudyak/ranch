package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";

    // SoundPool (1) - set limit for loading sounds into memory
    public static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds;
    // SoundPool (2) - add instance variable
    private SoundPool mSoundPool;

    // getters and setters
    public List<Sound> getSounds() {
        return mSounds;
    }

    public BeatBox(Context context) {
        mAssets = context.getAssets();

        // SoundPool (3) - build sound pool
        buildSoundPool();

        loadSounds();
    }

    // play and release sound pool
    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    public void release() {
        mSoundPool.release();
    }

    // helper methods
    private void buildSoundPool() {

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(MAX_SOUNDS)
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                )
                .build();
    }
    private void loadSounds() {

        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        mSounds = new ArrayList<>();
        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                mSounds.add(sound);
                load(sound);
            } catch (IOException e) {
                Log.e(TAG, "can not load file" + filename, e);
            }
        }
    }
    private void load(Sound sound) throws IOException {
        int soundId = mSoundPool.load(mAssets.openFd(sound.getAssetPath()), 1);
        sound.setSoundId(soundId);
    }

}
