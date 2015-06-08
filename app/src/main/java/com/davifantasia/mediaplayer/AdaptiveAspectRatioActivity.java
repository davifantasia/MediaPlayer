package com.davifantasia.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.IOException;


public class AdaptiveAspectRatioActivity extends AppCompatActivity {

    private final static String TAG = AdaptiveAspectRatioActivity.class.getSimpleName();

    private ProgressBar mProgressBar;
    private SurfaceView mVideoSurfaceView;
    private SurfaceHolder mVideoSurfaceHolder;
    private ImageButton mPlayPauseImageButton;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaptive_aspect_ratio);

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
        String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

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
            mMediaPlayer.setOnPreparedListener(new MyMediaPlayerOnPreparedListener());
            mMediaPlayer.prepareAsync();


        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Problem with url string.");

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyMediaPlayerOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //Get the dimensions of the video
            int videoWidth = mMediaPlayer.getVideoWidth();
            int videoHeight = mMediaPlayer.getVideoHeight();

            //Get the width of the screen
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;


            //Get the SurfaceView layout parameters
            android.view.ViewGroup.LayoutParams lp = mVideoSurfaceView.getLayoutParams();

            //Set the width of the SurfaceView to the width of the screen
            lp.width = screenWidth;

            //Set the height of the SurfaceView to match the aspect ratio of the video
            //be sure to cast these as floats otherwise the calculation will likely be 0
            lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);

            //Commit the layout parameters
            mVideoSurfaceView.setLayoutParams(lp);

            //Start video
            mProgressBar.setVisibility(View.GONE);
            mMediaPlayer.start();
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
                    Log.d(TAG, "It is PLAY button.");

                    if (!mMediaPlayer.isPlaying()) {
                        Log.d(TAG, "Start PLAY process.");
                        // Change to play.
                        playVideo();
                    }
                } else {
                    Log.d(TAG, "It is PAUSE button.");

                    if (mMediaPlayer.isPlaying()) {
                        Log.d(TAG, "Start PAUSE process.");
                        // Change to pause.
                        pauseVideo();
                    }
                }
            }
        });
    }

    private void playVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mPlayPauseImageButton.setTag(getResources().getString(R.string.pause));
            mPlayPauseImageButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    private void pauseVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mPlayPauseImageButton.setTag(getResources().getString(R.string.play));
            mPlayPauseImageButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adaptive_aspect_ratio, menu);
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
