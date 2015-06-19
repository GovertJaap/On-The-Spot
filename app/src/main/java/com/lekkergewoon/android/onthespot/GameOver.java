package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GameOver extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    GoogleApiClient mGoogleApiClient;
    TextView tScore, tHighScore;
    int score, highscore, levelNumber;
    String scoreKey;
    SharedPreferences prefs;
    FullMenu musicClass = new FullMenu();
    boolean transition = false;
    boolean goToMenu;

    //disables the default android backbutton
    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_game_over);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        updateScore();
        try {
            showScore();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        MenuButton();
        ScoreButton();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (goToMenu == true && musicClass.musicOn == true) {
            musicClass.mpPlayer = musicClass.mpPlayer.create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        }
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        transition = false;
        super.onResume();
    }

    private void updateScore() {
        score = getIntent().getExtras().getInt("score");
        //score = score - 5000;
        if (score < 0) {
            score = 0;
        }

        levelNumber = getIntent().getExtras().getInt("level");
        scoreKey = "level" + levelNumber + "score";

        prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = prefs.edit();
        highscore = prefs.getInt(scoreKey, 0); //0 is the default value

        if (score > highscore) {
            highscore = score;
            editor.putInt(scoreKey, highscore);
            editor.commit();
        }
    }

    private void showScore() throws IOException, JSONException {
        tScore = (TextView) findViewById(R.id.scoreGameOver);
        tScore.setText("Score: " + score);
        tHighScore = (TextView) findViewById(R.id.highscoreGameOver);
        tHighScore.setText("Highscore: " + prefs.getInt(scoreKey, 0));

        ImageView tLevelImage = (ImageView) findViewById(R.id.starViewGameOver);

        JSONObject obj = new JSONObject(loadJSONFromAsset());
        JSONArray jArray = obj.getJSONArray("main");
        JSONObject levelData = jArray.getJSONObject(levelNumber-1);
        int star1Requirement = levelData.getInt("1-Star");
        int star2Requirement = levelData.getInt("2-Star");
        int star3Requirement = levelData.getInt("3-Star");

        if (prefs.getInt(scoreKey, 0) > star3Requirement) {
            tLevelImage.setImageResource(R.drawable.stars_3);
        }
        else if (prefs.getInt(scoreKey, 0) > star2Requirement) {
            tLevelImage.setImageResource(R.drawable.stars_2);
        }
        else if (prefs.getInt(scoreKey, 0) > star1Requirement) {
            tLevelImage.setImageResource(R.drawable.stars_1);
        }
        else {
            tLevelImage.setImageResource(R.drawable.stars_0);
        }
        tLevelImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void MenuButton() {
        Button button = (Button) findViewById(R.id.GotoMenuGameOver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu = true;
                finish();
            }
        });
    }

    private void ScoreButton() {
        Button button = (Button) findViewById(R.id.Leaderboards);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelNumber = getIntent().getExtras().getInt("level");
                switch (levelNumber) {
                    case 1:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQAA"), 1);

                        break;
                    case 2:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQAQ"), 1);
                        break;
                    case 3:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQAg"), 1);
                        break;
                    case 4:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQAw"), 1);
                        break;
                    case 5:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQBA"), 1);
                        break;
                    case 6:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQBQ"), 1);
                        break;
                    case 7:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQBg"), 1);
                        break;
                    case 8:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQBw"), 1);
                        break;
                    case 9:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQCA"), 1);
                        break;
                    case 10:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQCQ"), 1);
                        break;
                    case 11:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQCg"), 1);
                        break;
                    case 12:
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, "CgkIvsGnkbUREAIQCw"), 1);
                        break;
                }
                finish();

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            transition = true;
            Intent activity = new Intent(GameOver.this, MainActivity.class);
            activity.putExtra("level", levelNumber - 1);
            startActivity(activity);
            finish();
        }
        return true;
    }

    public String loadJSONFromAsset() throws IOException {
        String json;
        try {
            InputStream is = getAssets().open("leveldata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }

        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onConnected(Bundle bundle) {

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
