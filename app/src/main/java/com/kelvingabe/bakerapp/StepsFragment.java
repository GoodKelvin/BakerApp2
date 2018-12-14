package com.kelvingabe.bakerapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kelvingabe.bakerapp.RecipeActivity._ARRAY;
import static com.kelvingabe.bakerapp.RecipeActivity._INDEX;


public class StepsFragment extends Fragment implements View.OnClickListener {
    private static final String MEDIA_POSITION = "media_current_position";
    private static final String MEDIA_WINDOW_INDEX = "media_current_window_index";
    private static final String MEDIA_PLAY_ON_READY = "play_when_ready";
    @BindView(R.id.step_image)
    ImageView stepImageView;
    @BindView(R.id.step_instruction_text)
    @Nullable
    TextView stepInstructionTextView;
    @BindView(R.id.step_video)
    PlayerView mExoPlayerView;
    @BindView(R.id.previous_button)
    @Nullable
    Button previousButton;
    @BindView(R.id.next_button)
    @Nullable
    Button nextButton;
    private String TAG = "StepsFragment";
    private int stepToDisplayIndex;
    private List<Step> stepList;
    //variables to store the position of player before it is destroyed
    private int mMediaWindowIndex;
    private long mMediaPosition;
    private boolean mMediaPlayOnReady = true;


    private SimpleExoPlayer simpleExoPlayer;


    public StepsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelableArrayList(_ARRAY) != null) {
            stepList = getArguments().getParcelableArrayList(_ARRAY);
            stepToDisplayIndex = getArguments().getInt(_INDEX);
            Log.d(TAG, "StepToDisplay " + stepToDisplayIndex);
        }

        if (savedInstanceState != null) {
            mMediaWindowIndex = savedInstanceState.getInt(MEDIA_WINDOW_INDEX);
            mMediaPosition = savedInstanceState.getLong(MEDIA_POSITION);
            stepToDisplayIndex = savedInstanceState.getInt(_INDEX);
            Log.d(TAG, "CRT " + mMediaWindowIndex + " " + mMediaPosition);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        if (nextButton != null && previousButton != null) {
            nextButton.setOnClickListener(this);
            previousButton.setOnClickListener(this);
            setButtonState();
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            showStep();
            initializePlayer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            showStep();
            initializePlayer();
        }

    }

    @Override
    public void onPause() {
        saveMediaState();
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releaseMediaPlayer();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseMediaPlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(MEDIA_POSITION, mMediaPosition);
        outState.putInt(MEDIA_WINDOW_INDEX, mMediaWindowIndex);
        outState.putBoolean(MEDIA_PLAY_ON_READY, mMediaPlayOnReady);
        outState.putInt(_INDEX, stepToDisplayIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                if (stepToDisplayIndex < stepList.size() - 1) {
                    stepToDisplayIndex++;
                }
                break;
            case R.id.previous_button:
                if (stepToDisplayIndex > 0) {
                    stepToDisplayIndex--;
                }
                break;
        }
        setButtonState();
        showStep();
        resetMediaState();
        initializePlayer();
    }

    private void resetMediaState() {
        mMediaWindowIndex = 0;
        mMediaPosition = 0;
    }


    private void saveMediaState() {
        mMediaPosition = simpleExoPlayer.getCurrentPosition();
        mMediaWindowIndex = simpleExoPlayer.getCurrentWindowIndex();
        mMediaPlayOnReady = simpleExoPlayer.getPlayWhenReady();
    }


    private void setButtonState() {
        int maxIndex = stepList.size() - 1;
        if (stepToDisplayIndex > 0) {
            if (previousButton != null) previousButton.setEnabled(true);
        }

        if (stepToDisplayIndex == 0) {
            if (previousButton != null) previousButton.setEnabled(false);

        }

        if (stepToDisplayIndex == maxIndex) {
            if (nextButton != null) nextButton.setEnabled(false);
        }

        if (stepToDisplayIndex < maxIndex) {
            if (nextButton != null) nextButton.setEnabled(true);
        }
    }

    private void releaseMediaPlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    private void showStep() {

        if (stepInstructionTextView != null) {
            stepInstructionTextView.setText(stepList.get(stepToDisplayIndex).description);
        }

        if (!TextUtils.isEmpty(stepList.get(stepToDisplayIndex).videoURL)) {
            stepImageView.setVisibility(View.INVISIBLE);
            mExoPlayerView.setVisibility(View.VISIBLE);

        } else
        {
            mExoPlayerView.setVisibility(View.INVISIBLE);
            stepImageView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(stepList.get(stepToDisplayIndex).thumbnailURL)) {

                Picasso.get().load(Uri.parse(stepList.get(stepToDisplayIndex).thumbnailURL))
                        .error(R.drawable.ic_error_red_185dp)
                        .into(stepImageView);
            } else // image is not available show default image
            {
                Picasso.get().load(R.drawable.default_recipe).into(stepImageView);
            }
        }

    }

    private void initializePlayer() {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), "BakerApp");
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(simpleExoPlayer);

        }
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(stepList.get(stepToDisplayIndex).videoURL));
        simpleExoPlayer.prepare(videoSource, true, false);
        simpleExoPlayer.setPlayWhenReady(mMediaPlayOnReady);
        simpleExoPlayer.seekTo(mMediaWindowIndex, mMediaPosition);
    }
}
