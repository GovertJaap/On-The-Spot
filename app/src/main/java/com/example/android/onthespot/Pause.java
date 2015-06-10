package com.example.android.onthespot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Pause extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_pause);

        continueButton();
        restartButton();
        menuButton();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void continueButton() {
        Button button = (Button) findViewById(R.id.pauseContinue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void menuButton() {
        Button button = (Button) findViewById(R.id.pauseMenu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(Pause.this, MenuActivity.class);
                MainActivity.getInstance().finish();
                startActivity(activity);
                finish();
            }
        });
    }
}
