package com.example.android.bakingapp;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ProgressBar mProgerssBar;
    private static final String TAG = StepFragment.class.getSimpleName();
    private String mUri;
    private Step mStep;
    private TextView mInstructionsView;
    private ImageView mNextStep;
    private ImageView mPreviousStep;
    private int mPosition;
    private boolean mIsFullScreen;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInstructionsView.setText(mStep.getDescription());
        mPosition = getArguments().getInt(DetailActivity.STEP_POSITION_KEY);
        int size = getArguments().getInt(DetailActivity.STEP_SIZE_KEY);
        initializeUri();
        if(mPosition > 0 && !mIsFullScreen) {
            mPreviousStep.setVisibility(View.VISIBLE);
        }
        if(mPosition < size-1 && !mIsFullScreen) {
            mNextStep.setVisibility(View.VISIBLE);
        }
        if (mUri == null) {
            Log.d(TAG, "uri null " + mPosition);
            mPlayerView.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "uri not null " + mPosition);
            initializePlayer(Uri.parse(mUri));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        // Inflate the layout for this fragment
        mStep = getArguments().getParcelable(DetailActivity.STEP_KEY);
        initializeUri();
        mIsFullScreen = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE
                && !getResources().getBoolean(R.bool.isTablet) && mUri != null;
        // Initialize the player view.
        mPlayerView = view.findViewById(R.id.playerView);
        mProgerssBar = view.findViewById(R.id.pb_exoplayer);
        mInstructionsView = view.findViewById(R.id.tv_step_instructions);
        mNextStep = view.findViewById(R.id.next_step);
        mPreviousStep = view.findViewById(R.id.previous_step);
        if(mIsFullScreen) {
            mInstructionsView.setVisibility(View.GONE);
            mNextStep.setVisibility(View.GONE);
            mNextStep.setVisibility(View.GONE);
            View decorView = getActivity().getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            mPlayerView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;

        }
        return view;
    }

    private void initializeUri() {
        if(mStep.getVideoUrl().length() > 0) {
            mUri = mStep.getVideoUrl();
        } else if(mStep.getThumbnailUrl().length() > 0) {
            mUri = mStep.getThumbnailUrl();
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.addListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == ExoPlayer.STATE_BUFFERING) {
            mProgerssBar.setVisibility(View.VISIBLE);
        } else {
            mProgerssBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
