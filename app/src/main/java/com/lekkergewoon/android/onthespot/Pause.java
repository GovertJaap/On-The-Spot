package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Pause extends Activity {

    FullMenu musicClass = new FullMenu();
    boolean goToMenu = false;

    @Override
    public void onBackPressed() {
        MainActivity.backFromPause = true;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_pause);

        TextView tv = (TextView) findViewById(R.id.pauseText);
        Typeface font= Typeface.createFromAsset(getAssets(), "arcade_regular.ttf");
        tv.setTypeface(font);

        continueButton();
        restartButton();
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

        super.onResume();
    }

    private void continueButton() {
        Button button = (Button) findViewById(R.id.pauseContinue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.backFromPause = true;
                finish();
                MainActivity.getInstance().transition = true;
            }
        });
    }
    private void restartButton() {
        Button button = (Button) findViewById(R.id.pauseRestart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(Pause.this, MainActivity.class);
                activity.putExtra("level", getIntent().getExtras().getInt("level") - 1);
                MainActivity.getInstance().finish();
                startActivity(activity);
                finish();
            }
        });
    }

    private void lvlSelectButton() {
        Button button = (Button) findViewById(R.id.pauseLvlSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicClass.mpPlayer.release();
                goToMenu = true;
                Intent activity = new Intent(Pause.this, LvlSelectActivity.class);
                MainActivity.getInstance().finish();
                startActivity(activity);
                finish();
            }
        });
    }
}
