package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;

import com.lekkergewoon.android.onthespot.R;

public class LeaderboardsActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    GoogleApiClient mGoogleApiClient;
    TextView tLeaderboard;
    int leaderboard;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        leaderboard = getIntent().getExtras().getInt("leaderboard");
        super.onCreate(savedInstanceState);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        setContentView(R.layout.activity_leaderboards);

        TextView tv = (TextView) findViewById(R.id.leaderboardText);
        Typeface font= Typeface.createFromAsset(getAssets(), "arcade_regular.ttf");
        tv.setTypeface(font);

        showleaderboard();
        backButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void showleaderboard() {
        leaderboard = getIntent().getExtras().getInt("leaderboard");
        tLeaderboard = (TextView) findViewById(R.id.sample);
        tLeaderboard.setText("leaderboard: " + leaderboard);
    }

    private void backButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButtonLeaderboard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        leaderboard = getIntent().getExtras().getInt("level");
        switch (leaderboard) {
            case 1:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQAA"), 1);
                break;
            case 2:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQAQ"), 2);
                break;
            case 3:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQAg"), 3);
                break;
            case 4:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQAw"), 4);
                break;
            case 5:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQBA"), 5);
                break;
            case 6:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQBQ"), 6);
                break;
            case 7:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQBg"), 7);
                break;
            case 8:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQBw"), 8);
                break;
            case 9:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQCA"), 9);
                break;
            case 10:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQCQ"), 10);
                break;
            case 11:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQCg"), 11);
                break;
            case 12:
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkIvsGnkbUREAIQCw"), 12);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(dialogFragment.getFragmentManager(), "errordialog");

    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}