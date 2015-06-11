package com.davifantasia.mediaplayer.utils;

import android.media.MediaPlayer;

/**
 * Adds my custom functionality to media player class;
 *
 * Created by Adewale on 6/11/2015.
 */
public class MyMediaPlayer extends MediaPlayer {

    private boolean mMute = false;

    public void mute() {
        setVolume(0.0f, 0.0f);
        mMute = true;
    }

    public void unMute() {
        setVolume(1.0f, 1.0f);
        mMute = false;
    }

    public boolean isMute() {
        return mMute;
    }
}
