package com.example.android.onthespot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mike on 6-6-2015.
 */
public class LvlWon extends ActionBarActivity {
    TextView tScore, tHighScore;
    int score, highscore, levelNumber;
    String scoreKey;
    SharedPreferences prefs;

    //disables the default android backbutton
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl_won);

        updateScore();
        showScore();
        MenuButton();
        ScoreButton();
        NextButton();
        RestartButton();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void updateScore() {
        score = getIntent().getExtras().getInt("score");
        levelNumber = getIntent().getExtras().getInt("level");
        scoreKey = "level" + levelNumber + "score";
        String unlockKey = "level" + (levelNumber + 1) + "unlock";

        prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = prefs.edit();
        highscore = prefs.getInt(scoreKey, 0); //0 is the default value

        if (score > highscore) {
            highscore = score;
            editor.putInt(scoreKey, highscore);
        }

        editor.putBoolean(unlockKey, true);
        editor.commit();
    }

    private void showScore() {
        tScore = (TextView) findViewById(R.id.scoreLevelWon);
        tScore.setText("Score: " + score);
        tHighScore = (TextView) findViewById(R.id.highscoreLevelWon);
        tHighScore.setText("Highscore: " + prefs.getInt(scoreKey, 0));
    }

    private void MenuButton() {
        Button button = (Button) findViewById(R.id.GotoMenu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LvlWon.this, MenuActivity.class));
            }
        });
    }

    private void ScoreButton() {
        Button button = (Button) findViewById(R.id.Leaderboards);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LvlWon.this, "Leaderboards are not available.", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(GameOver.this, MainActivity.class));
                //change to leaderboards.class once integrated
            }
        });
    }

    private void NextButton() {
        Button button = (Button) findViewById(R.id.nextlvl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlWon.this, MainActivity.class);
                activity.putExtra("level", levelNumber);
                startActivity(activity);
            }
        });
    }

    private void RestartButton() {
        Button button = (Button) findViewById(R.id.restart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlWon.this, MainActivity.class);
                activity.putExtra("level", levelNumber - 1);
                startActivity(activity);
            }
        });
    }
}
