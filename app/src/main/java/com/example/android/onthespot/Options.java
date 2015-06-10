package com.example.android.onthespot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.onthespot.R;

public class Options extends Activity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String scoreKey;
    String unlockKey;
    FullMenu musicClass = new FullMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_options);

        prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        editor = prefs.edit();

        if (musicClass.playing == false) {
            musicClass.mpPlayer = musicClass.createMusic().create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        } else { }

        resetScores();
        resetUnlocks();
        unlockAllLevels();
        backButton();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void resetScores() {
        Button button = (Button) findViewById(R.id.resetScores);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Options.this, "Your scores have been reset", Toast.LENGTH_LONG).show();
                for (int i = 1; i < 13; i++) {
                    scoreKey = "level" + i + "score";
                    editor.putInt(scoreKey, 0);
                    editor.commit();
                }
            }
        });
    }

    private void resetUnlocks() {
        Button button = (Button) findViewById(R.id.resetUnlocks);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Options.this, "All levels have been reset", Toast.LENGTH_LONG).show();
                for (int i = 1; i < 13; i++) {
                    scoreKey = "level" + i + "score";
                    unlockKey = "level" + i + "unlock";
                    editor.putInt(scoreKey, 0);
                    editor.putBoolean(unlockKey, false);
                    editor.commit();
                }
            }
        });
    }

    private void unlockAllLevels() {
        Button button = (Button) findViewById(R.id.unlockAll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Options.this, "All levels have been unlocked", Toast.LENGTH_LONG).show();
                for (int i = 1; i < 13; i++) {
                    unlockKey = "level" + i + "unlock";
                    editor.putBoolean(unlockKey, true);
                    editor.commit();
                }
            }
        });
    }

    private void backButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButtonOptions);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
