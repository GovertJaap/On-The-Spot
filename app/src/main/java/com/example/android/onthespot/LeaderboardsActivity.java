package com.example.android.onthespot;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.onthespot.R;

public class LeaderboardsActivity extends Activity{
    TextView tLeaderboard;
    int leaderboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        showleaderboard();
        backButton();
    }

    private void showleaderboard() {
        leaderboard = getIntent().getExtras().getInt("leaderboard");
        tLeaderboard = (TextView) findViewById(R.id.sample);
        tLeaderboard.setText("leaderboard: " + leaderboard);
    }

    private void backButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButtonLeaderboard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
