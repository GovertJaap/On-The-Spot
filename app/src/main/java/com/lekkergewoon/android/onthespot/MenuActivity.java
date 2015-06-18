package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MenuActivity extends Activity {

    FullMenu musicClass = new FullMenu();
    boolean transition;

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (musicClass.musicOn == true) {
                            musicClass.mpPlayer.release();
                            musicClass.playing = false;
                        }
                        transition = true;
                        finish();
                        break;

                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("Are you sure you want to quit?")
                .setNegativeButton("Yes, siree!", dialogClickListener)
                .setPositiveButton("No way out", dialogClickListener).show();
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
        if (musicClass.exit == true && musicClass.musicOn == true && musicClass.playing == false) {
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        }

        musicClass.exit = false;
        transition = false;
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_menu);

        if (musicClass.playing == false && musicClass.musicOn == true) {
            musicClass.mpPlayer = musicClass.createMusic().create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        }

        singlePlayButton();
        multiplayButton();
        scoreButton();
        optionsButton();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void singlePlayButton() {
        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                transition = true;
                Intent activity = new Intent(MenuActivity.this, LvlSelectActivity.class);
                startActivity(activity);
            }
        });
    }


    private void multiplayButton() {
        Button playButton = (Button) findViewById(R.id.multiplayerButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (musicClass.musicOn == true) {
                    musicClass.mpPlayer.release();
                    musicClass.playing = false;
                }

                transition = true;
                Intent activity = new Intent(MenuActivity.this, MainActivity.class);
                activity.putExtra("level", 12);
                startActivity(activity);
            }
        });
    }

    private void scoreButton() {
        Button playButton = (Button) findViewById(R.id.leaderboardButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                transition = true;
                Intent activity = new Intent(MenuActivity.this, chooseLeaderboardActivity.class);
                startActivity(activity);
            }
        });
    }

    private void optionsButton() {
        Button playButton = (Button) findViewById(R.id.optionsButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                transition = true;
                Intent activity = new Intent(MenuActivity.this, Options.class);
                startActivity(activity);
            }
        });
    }
}
