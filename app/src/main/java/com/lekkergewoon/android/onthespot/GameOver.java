package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

public class GameOver extends Activity{

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_game_over);

        TextView tv = (TextView) findViewById(R.id.gameOver);
        Typeface font = Typeface.createFromAsset(getAssets(), "arcade_regular.ttf");
        tv.setTypeface(font);

        tv = (TextView) findViewById(R.id.tapToRestart);
        font = Typeface.createFromAsset(getAssets(), "arcade_regular.ttf");
        tv.setTypeface(font);

        tv = (TextView) findViewById(R.id.scoreGameOver);
        font = Typeface.createFromAsset(getAssets(), "arcade_regular.ttf");
        tv.setTypeface(font);

        tv = (TextView) findViewById(R.id.highscoreGameOver);
        font = Typeface.createFromAsset(getAssets(), "arcade_regular.ttf");
        tv.setTypeface(font);


        updateScore();
        try {
            showScore();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        lvlSelectButton();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        if (goToMenu == true && musicClass.musicOn == true) {
            musicClass.mpPlayer = musicClass.mpPlayer.create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        }
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

    private void lvlSelectButton() {
        Button button = (Button) findViewById(R.id.GotoLvlSelectGameOver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu = true;
                transition = true;
                Intent activity = new Intent(GameOver.this, LvlSelectActivity.class);
                startActivity(activity);
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
}