package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mike on 6-6-2015.
 */
public class LvlWon extends Activity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_lvl_won);

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
}
