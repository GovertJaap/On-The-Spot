package com.example.android.onthespot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Mike on 6-6-2015.
 */
public class LvlWon extends ActionBarActivity {
    //disables the default android backbutton
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl_won);

        MenuButton();
        ScoreButton();
        NextButton();
        RestartButton();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                activity.putExtra("level", (getIntent().getExtras().getInt("level")+1));
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
                activity.putExtra("level", getIntent().getExtras().getInt("level"));
                startActivity(activity);
            }
        });
    }
}
