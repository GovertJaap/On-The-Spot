package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mike on 6-6-2015.
 */
public class LvlWon extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


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
    FullMenu musicClass = new FullMenu();
    boolean transition = false;
    boolean goToMenu = false;

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
        setContentView(R.layout.activity_lvl_won);

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
        NextButton();
        RestartButton();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        levelNumber = getIntent().getExtras().getInt("level");
        scoreKey = "level" + levelNumber + "score";
        String unlockKey = "level" + (levelNumber + 1) + "unlock";

        SharedPreferences prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = prefs.edit();
        highscore = prefs.getInt(scoreKey, 0); //0 is the default value

        if (score > highscore) {
            highscore = score;
            editor.putInt(scoreKey, highscore);
        }

        editor.putBoolean(unlockKey, true);
        editor.commit();
    }

    private void showScore() throws IOException, JSONException {
        tScore = (TextView) findViewById(R.id.scoreWon);
        tScore.setText("Score: " + score);
        tHighScore = (TextView) findViewById(R.id.highscoreWon);
        SharedPreferences prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        tHighScore.setText("Highscore: " + prefs.getInt(scoreKey, 0));

        ImageView tLevelImage = (ImageView) findViewById(R.id.starViewWon);

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

    private void MenuButton() {
        Button button = (Button) findViewById(R.id.GotoMenuWon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent activity = new Intent(LvlWon.this, MenuActivity.class);
//                startActivity(activity);
                goToMenu = true;
                transition = true;
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
                Intent activity = new Intent(LvlWon.this, LeaderboardsActivity.class);
                activity.putExtra("leaderboard",levelNumber);
                startActivity(activity);
                finish();

            }
        });
    }

    private void NextButton() {
        Button button = (Button) findViewById(R.id.nextlvl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelNumber == 12) {
                    Toast.makeText(LvlWon.this, "Sorry, this was the last level!", Toast.LENGTH_LONG).show();
                }
                else {
                    musicClass.mpPlayer = null;
                    transition = true;
                    Intent activity = new Intent(LvlWon.this, MainActivity.class);
                    activity.putExtra("level", levelNumber);
                    startActivity(activity);
                    finish();
                }
            }
        });
    }

    private void RestartButton() {
        Button button = (Button) findViewById(R.id.restart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicClass.mpPlayer = null;
                transition = true;
                Intent activity = new Intent(LvlWon.this, MainActivity.class);
                activity.putExtra("level", levelNumber - 1);
                startActivity(activity);
                finish();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        levelNumber = getIntent().getExtras().getInt("level");
        score = getIntent().getExtras().getInt("score");
        switch (levelNumber) {
            case 1:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQAA", score);
                break;
            case 2:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQAQ", score);
                break;
            case 3:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQAg", score);
                break;
            case 4:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQAw", score);
                break;
            case 5:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQBA", score);
                break;
            case 6:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQBQ", score);
                break;
            case 7:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQBg", score);
                break;
            case 8:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQBw", score);
                break;
            case 9:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQCA", score);
                break;
            case 10:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQCQ", score);
                break;
            case 11:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQCg", score);
                break;
            case 12:
                Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIvsGnkbUREAIQCw", score);
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
        dialogFragment.show(dialogFragment.getFragmentManager(), "errordialog" );

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

