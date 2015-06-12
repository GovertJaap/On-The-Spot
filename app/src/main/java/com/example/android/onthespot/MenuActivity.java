package com.example.android.onthespot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class MenuActivity extends Activity {

    FullMenu musicClass = new FullMenu();

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        musicClass.mpPlayer.release();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_menu);

        if (musicClass.playing == false) {
            musicClass.mpPlayer = musicClass.createMusic().create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        } else { }

//        boolean playing = getIntent().getExtras().getBoolean("playing");
//        if (playing == false) {
//            mpPlayer = MediaPlayer.create(this, R.raw.menu);
//            mpPlayer.start();
//        } else { mpPlayer.release(); }
//        playing = true;

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
                Toast.makeText(MenuActivity.this, "Leaderboards are not available.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void optionsButton() {
        Button playButton = (Button) findViewById(R.id.optionsButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MenuActivity.this, Options.class);
                startActivity(activity);
            }
        });
    }
}
