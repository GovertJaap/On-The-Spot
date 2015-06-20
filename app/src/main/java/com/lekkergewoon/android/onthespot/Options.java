package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lekkergewoon.android.onthespot.R;

public class Options extends Activity {
    SharedPreferences.Editor editor;
    String scoreKey;
    String unlockKey;
    FullMenu musicClass = new FullMenu();
    boolean transition = false;
    SharedPreferences prefs;

    @Override
    public void onBackPressed() {
        transition = true;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_options);

        prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        editor = prefs.edit();

        resetScores();
        resetUnlocks();
        unlockAllLevels();
        backButton();
        musicButton();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        if (musicClass.mpPlayer != null && transition != true && musicClass.musicOn == true) {
            musicClass.mpPlayer.pause();
            musicClass.length = musicClass.mpPlayer.getCurrentPosition();
            musicClass.playing = false;
            musicClass.exit = true;
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        if (musicClass.musicOn == false && musicClass.playing == false)
        musicClass.mpPlayer = musicClass.mpPlayer.create(this, R.raw.menu);

        transition = false;
        super.onResume();
    }

    private void updateMusicPref() {
        prefs = this.getSharedPreferences("MusicPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = prefs.edit();
        musicClass.musicOn = prefs.getBoolean("music on: ", musicClass.musicOn);

        if (musicClass.musicOn == true) {
            musicClass.mpPlayer.pause();
            musicClass.musicOn = false;
            musicClass.playing = false;
            Toast.makeText(Options.this, "Music is off.", Toast.LENGTH_LONG).show();

        } else {
            musicClass.mpPlayer.start();
            musicClass.musicOn = true;
            musicClass.playing = true;
            Toast.makeText(Options.this, "Music is on. Cheers.", Toast.LENGTH_LONG).show();
        }
        editor.putBoolean("music on: ", musicClass.musicOn);
        editor.commit();
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
                transition = true;
                finish();
            }
        });
    }

    private void musicButton() {
        Button button = (Button) findViewById(R.id.musicChange);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMusicPref();
            }
        });
    }
}
