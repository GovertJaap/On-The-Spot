package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Luuk on 15-6-2015.
 */
public class MultiplayerEnd extends Activity {
    TextView tScore1, tScore2, tPlayerWon;
    int score1, score2, levelNumber;
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
        setContentView(R.layout.activity_mulitplayer_end);

        showScore();
        MenuButton();
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

    private void showScore() {
        levelNumber = getIntent().getExtras().getInt("level");
        score1 = getIntent().getExtras().getInt("score1");
        score2  = getIntent().getExtras().getInt("score2");
        tPlayerWon = (TextView) findViewById(R.id.playerWon);
        if (score1 >= score2){
            tPlayerWon.setText("Player 1 Won!");
        }
        else {
            tPlayerWon.setText("Player 2 Won!");
        }

        tScore1 = (TextView) findViewById(R.id.player1Score);
        tScore1.setText("Player 1: " + score1);
        tScore2 = (TextView) findViewById(R.id.player2Score);
        tScore2.setText("Player 2: " + score2);
    }

    private void MenuButton() {
        Button button = (Button) findViewById(R.id.GotoMenuMpWon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu = true;
                transition = true;
                finish();
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
                Intent activity = new Intent(MultiplayerEnd.this, MainActivity.class);
                activity.putExtra("level", levelNumber - 1);
                startActivity(activity);
                finish();
            }
        });
    }
}
