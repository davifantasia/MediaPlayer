package com.davifantasia.mediaplayer;

import android.graphics.drawable.AnimationDrawable;
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
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.davifantasia.mediaplayer.utils.MyMediaPlayer;

import java.io.IOException;


public class FixedAspectRatioInstagramActivity extends AppCompatActivity {

    private final static String TAG = FixedAspectRatioInstagramActivity.class.getSimpleName();

    private AlphaAnimation mFadeInAnim;
    private AlphaAnimation mFadeOutAnim;
    private ImageView mVideoPictureImageView;
    private ImageView mVideoLoadingImageView;
    private AnimationDrawable mVideoLoadingAnimation;
    private SurfaceView mVideoSurfaceView;
    private SurfaceHolder mVideoSurfaceHolder;
    private MyMediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_aspect_ratio_instagram);

        init();
    }

    private void init() {
        initAnims();
        initViews();
    }

    private  void initAnims() {
        mFadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim.setDuration(200);
        mFadeOutAnim = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim.setDuration(200);
    }

    private void initViews() {
        mVideoPictureImageView = (ImageView) findViewById(R.id.video_picture_image_view);
        initVideoLoadingImageView();
        initSurfaceView();
        initVideoSurfaceHolder();
    }

    private void initVideoLoadingImageView() {
        mVideoLoadingImageView = (ImageView) findViewById(R.id.video_loading_image_view);
        mVideoLoadingImageView.setBackgroundResource(R.drawable.video_loading);
        mVideoLoadingAnimation = (AnimationDrawable) mVideoLoadingImageView.getBackground();
    }

    private void initSurfaceView() {
        mVideoSurfaceView = (SurfaceView) findViewById(R.id.video_surface_view);
        mVideoSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    if (mMediaPlayer.isMute()) {
                        mMediaPlayer.unMute();
                    } else {
                        mMediaPlayer.mute();
                    }
                }
            }
        });
    }

    private void initVideoSurfaceHolder() {
        mVideoSurfaceHolder = mVideoSurfaceView.getHolder();
        mVideoSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
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
        String url = getResources().getString(R.string.big_buck_bunny_url);

        try {
            mMediaPlayer = new MyMediaPlayer();
            mMediaPlayer.setDisplay(mVideoSurfaceHolder);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setVolume(0.0f, 0.0f);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setOnInfoListener(
                    new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {

                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                                // Fade in video loading gif.
                                mVideoLoadingImageView.setVisibility(View.VISIBLE);
                                if (mVideoLoadingImageView.getAlpha() == 0) {
                                    mVideoLoadingImageView.startAnimation(mFadeInAnim);
                                }
                            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                // Fade out video loading gif.
                                if (mVideoLoadingImageView.getAlpha() == 1) {
                                    mVideoLoadingImageView.startAnimation(mFadeOutAnim);
                                }
                                mVideoLoadingImageView.setVisibility(View.GONE);

                                // Fade out video picture.
                                if (mVideoPictureImageView.getVisibility() == View.VISIBLE) {
                                    mVideoPictureImageView.startAnimation(mFadeOutAnim);
                                    mVideoPictureImageView.setVisibility(View.GONE);
                                }
                            }

                            // NOTE: Actual Instagram implementation as Instagram video does not buffer.
//                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//                                if (mVideoLoadingImageView.getAlpha() == 1) {
//                                    mVideoLoadingImageView.startAnimation(mFadeOutAnim);
//                                }
//                                mVideoLoadingImageView.setVisibility(View.GONE);
//
//                                // Fade out video picture.
//                                if (mVideoPictureImageView.getVisibility() == View.VISIBLE) {
//                                    mVideoPictureImageView.startAnimation(mFadeOutAnim);
//                                    mVideoPictureImageView.setVisibility(View.GONE);
//                                }
//                            }

                            return false;
                        }
                    }
            );
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
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

    private void playVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    private void pauseVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fixed_aspect_ratio_instagram, menu);
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus)
            mVideoLoadingAnimation.start();
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

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer = null;
        }

        super.onStop();
    }

}
