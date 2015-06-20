package com.davifantasia.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;


public class LiveStreamActivity extends AppCompatActivity {

    private final static String TAG = LiveStreamActivity.class.getSimpleName();

    private ProgressBar mProgressBar;
    private SurfaceView mVideoSurfaceView;
    private SurfaceHolder mVideoSurfaceHolder;
    private ImageButton mPlayPauseImageButton;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

        init();
    }

    private void init() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mVideoSurfaceView = (SurfaceView) findViewById(R.id.video_surface_view);
        initVideoSurfaceHolder();
        initPlayPauseImageButton();
    }

    private void initVideoSurfaceHolder() {
        mVideoSurfaceHolder = mVideoSurfaceView.getHolder();
        mVideoSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mProgressBar.setVisibility(View.VISIBLE);
                initMediaPlayer();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    private void initMediaPlayer() {
        Log.i(TAG, "Entered initMediaPlayer().");
        mPlayPauseImageButton.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);

        String url = getResources().getString(R.string.live_stream_link);

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDisplay(mVideoSurfaceHolder);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnInfoListener(
                    new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                                mProgressBar.setVisibility(View.VISIBLE);
                            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    }
            );
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "mMediaPlayer Error - Type: " + what + ", Extra: " + extra);
                    Toast.makeText(getApplicationContext(),
                            "Problem playing media, please try in a short while.",
                            Toast.LENGTH_LONG).show();
                    resetPlayBack();

                    return false;
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Start video.
                    mProgressBar.setVisibility(View.GONE);
                    mMediaPlayer.start();
                    mPlayPauseImageButton.setEnabled(true);
                }
            });
            mMediaPlayer.prepareAsync();


        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Problem with url string.");

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetPlayBack() {
        Log.i(TAG, "Entered resetPlayBack().");

        // Set playback to not playing state.
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer = null;

            mProgressBar.setVisibility(View.GONE);
            mPlayPauseImageButton.setTag(getResources().getString(R.string.play));
            mPlayPauseImageButton.setImageResource(android.R.drawable.ic_media_play);
            mPlayPauseImageButton.setEnabled(true);
        }
    }

    private void initPlayPauseImageButton() {
        mPlayPauseImageButton = (ImageButton) findViewById(R.id.play_pause_image_button);
        mPlayPauseImageButton.setEnabled(false);
        mPlayPauseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPlayPauseImageButton.getTag().equals(getResources().getString(R.string.play))) {
                    Log.d(TAG, "It is PLAY button, start PLAY process.");
                    playVideo();
                } else {
                    Log.d(TAG, "It is PAUSE button, start PAUSE process.");
                    pauseVideo();
                }
            }
        });
    }

    private void playVideo() {
        Log.i(TAG, "Entered playVideo().");

        if (mMediaPlayer != null) {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        } else {
            initMediaPlayer();
        }

        mPlayPauseImageButton.setTag(getResources().getString(R.string.pause));
        mPlayPauseImageButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void pauseVideo() {
        Log.i(TAG, "Entered pauseVideo().");

        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mPlayPauseImageButton.setTag(getResources().getString(R.string.play));
            mPlayPauseImageButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_stream, menu);
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

    @Override
    protected void onResume() {
        super.onResume();

        playVideo();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Entered onPause().");
        pauseVideo();

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Entered onStop().");

        mMediaPlayer.reset();
        mMediaPlayer = null;

        super.onStop();
    }
}
