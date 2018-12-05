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

import static com.kelvingabe.bakerapp.ViewRecipeDetailsActivity.STEP_LIST;
import static com.kelvingabe.bakerapp.ViewRecipeDetailsActivity.STEP_TO_DISPLAY_INDEX;


//Fragment to display the step detail
// uses picasso from square(http://square.github.io/picasso/) to load images
// uses Butter knife  from Jake Wharton (http://jakewharton.github.io/butterknife/) to bind views
public class StepDetailFragment extends Fragment implements View.OnClickListener {

    //key for storage of current play position
    private static final String MEDIA_PREVIOUS_POSITION = "media_current_position";
    private static final String MEDIA_PREVIOUS_WINDOW_INDEX = "media_current_window_index";
    private static final String MEDIA_PLAY_ON_READY = "play_when_ready";
    @BindView(R.id.step_image)
    ImageView mStepImage;
    @BindView(R.id.step_video)
    PlayerView mExoPlayerView;
    @BindView(R.id.step_instruction_text)
    @Nullable
    TextView mStepInstructionTextView;
    @BindView(R.id.previous_button)
    @Nullable
    Button mPreviousButton;
    @BindView(R.id.next_button)
    @Nullable
    Button mNextButton;
    private int mStepToDisplayIndex;
    private List<Step> mStepList;
    //variables to store the position of player before it is destroyed
    private int mMediaWindowIndex;
    private long mMediaPosition;
    private boolean mMediaPlayOnReady = true;


    private SimpleExoPlayer mSimpleExoPlayer;


    public StepDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the step to display
        if (getArguments() != null && getArguments().getParcelableArrayList(STEP_LIST) != null) {
            mStepList = getArguments().getParcelableArrayList(STEP_LIST);
            mStepToDisplayIndex = getArguments().getInt(STEP_TO_DISPLAY_INDEX);
            Log.d("XXX", "StepToDisplay " + mStepToDisplayIndex);
        }

        //get previous play position
        if (savedInstanceState != null) {
            mMediaWindowIndex = savedInstanceState.getInt(MEDIA_PREVIOUS_WINDOW_INDEX);
            mMediaPosition = savedInstanceState.getLong(MEDIA_PREVIOUS_POSITION);
            mStepToDisplayIndex = savedInstanceState.getInt(STEP_TO_DISPLAY_INDEX);
            Log.d("XXX", "CRT " + mMediaWindowIndex + " " + mMediaPosition);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        //if buttons are available on the UI, set them up
        if (mNextButton != null && mPreviousButton != null) {
            mNextButton.setOnClickListener(this);
            mPreviousButton.setOnClickListener(this);
            setButtonState();
        }
        return view;
    }


    //initialize player in opposing lifecycle method of the onStop
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            displayStep();
            initializePlayer();
        }
    }


    //initialize player in opposing lifecycle method of the onPause
    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mSimpleExoPlayer == null) {
            displayStep();
            initializePlayer();
        }

    }

    @Override
    public void onPause() {
        Log.d("XXX", "onPause");
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
        //save the current play position
        outState.putLong(MEDIA_PREVIOUS_POSITION, mMediaPosition);
        outState.putInt(MEDIA_PREVIOUS_WINDOW_INDEX, mMediaWindowIndex);
        outState.putBoolean(MEDIA_PLAY_ON_READY, mMediaPlayOnReady);
        outState.putInt(STEP_TO_DISPLAY_INDEX, mStepToDisplayIndex);
        super.onSaveInstanceState(outState);
        Log.d("XXX", "INSTASAVE " + outState);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                //if index not at max index increment index
                if (mStepToDisplayIndex < mStepList.size() - 1) {
                    mStepToDisplayIndex++;
                }
                break;
            case R.id.previous_button:
                //if index greater than 0, reduce index
                if (mStepToDisplayIndex > 0) {
                    mStepToDisplayIndex--;
                }
                break;
        }
        setButtonState();
        displayStep();
        //reset media state since we are moving to another media
        resetMediaState();
        initializePlayer();
    }

    //reset the media state
    private void resetMediaState() {
        mMediaWindowIndex = 0;
        mMediaPosition = 0;
    }


    private void saveMediaState() {
        Log.d("XXX", "saveMediaState");
        mMediaPosition = mSimpleExoPlayer.getCurrentPosition();
        mMediaWindowIndex = mSimpleExoPlayer.getCurrentWindowIndex();
        mMediaPlayOnReady = mSimpleExoPlayer.getPlayWhenReady();
    }

    //method to set the state of the buttons
    private void setButtonState() {
        int maxIndex = mStepList.size() - 1;
        if (mStepToDisplayIndex > 0) {
            if (mPreviousButton != null) mPreviousButton.setEnabled(true);
        }

        if (mStepToDisplayIndex == 0) {
            if (mPreviousButton != null) mPreviousButton.setEnabled(false);

        }

        if (mStepToDisplayIndex == maxIndex) {
            if (mNextButton != null) mNextButton.setEnabled(false);
        }

        if (mStepToDisplayIndex < maxIndex) {
            if (mNextButton != null) mNextButton.setEnabled(true);
        }
    }

    private void releaseMediaPlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    // TO DO refactor
    private void displayStep() {

        if (mStepInstructionTextView != null) {
            mStepInstructionTextView.setText(mStepList.get(mStepToDisplayIndex).description);
        }

        //check for video first
        if (!TextUtils.isEmpty(mStepList.get(mStepToDisplayIndex).videoURL)) {
            //show movie
            mStepImage.setVisibility(View.INVISIBLE);
            mExoPlayerView.setVisibility(View.VISIBLE);

        } else// movie not available check image
        {
            mExoPlayerView.setVisibility(View.INVISIBLE);
            mStepImage.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mStepList.get(mStepToDisplayIndex).thumbnailURL)) {

                Picasso.get().load(Uri.parse(mStepList.get(mStepToDisplayIndex).thumbnailURL))
                        .error(R.drawable.ic_error_red_185dp)
                        .into(mStepImage);
            } else // image is not available show default image
            {
                Picasso.get().load(R.drawable.default_recipe).into(mStepImage);
            }
        }

    }

    private void initializePlayer() {
        Log.d("XXX", "InitPlayer " + mSimpleExoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), "BakerApp");
        if (mSimpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(mSimpleExoPlayer);
            Log.d("XXX", "MediaState W:" + mMediaWindowIndex + " P:" + mMediaPosition + " R:" + mMediaPlayOnReady);
            Log.d("XXX", "Video " + mStepList.get(mStepToDisplayIndex).videoURL);

        }

        //setup movie to play
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mStepList.get(mStepToDisplayIndex).videoURL));
        mSimpleExoPlayer.prepare(videoSource, true, false);
        mSimpleExoPlayer.setPlayWhenReady(mMediaPlayOnReady);
        mSimpleExoPlayer.seekTo(mMediaWindowIndex, mMediaPosition);
    }
}
