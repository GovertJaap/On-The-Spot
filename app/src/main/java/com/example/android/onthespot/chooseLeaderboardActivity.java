package com.example.android.onthespot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.games.leaderboard.Leaderboards;


public class chooseLeaderboardActivity extends Activity {

    String leaderboardNumber;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_leaderboard);
        levelButtons();
        backButton();
    }

    private void levelButtons() {
        for (i = 1; i < 13; i++) {
            leaderboardNumber = "leaderboardselect" + i + "";

            int imageButtonId = getResources().getIdentifier(leaderboardNumber, "id", this.getPackageName());
            ImageButton tLeaderboardNumber = (ImageButton) findViewById(imageButtonId);

            tLeaderboardNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activity = new Intent(chooseLeaderboardActivity.this, LeaderboardsActivity.class);
                    switch(v.getId()) {
                        case R.id.leaderboardselect1:
                            activity.putExtra("leaderboard", 1);
                            break;
                        case R.id.leaderboardselect2:
                            activity.putExtra("leaderboard", 2);
                            break;
                        case R.id.leaderboardselect3:
                            activity.putExtra("leaderboard", 3);
                            break;
                        case R.id.leaderboardselect4:
                            activity.putExtra("leaderboard", 4);
                            break;
                        case R.id.leaderboardselect5:
                            activity.putExtra("leaderboard", 5);
                            break;
                        case R.id.leaderboardselect6:
                            activity.putExtra("leaderboard", 6);
                            break;
                        case R.id.leaderboardselect7:
                            activity.putExtra("leaderboard", 7);
                            break;
                        case R.id.leaderboardselect8:
                            activity.putExtra("leaderboard", 8);
                            break;
                        case R.id.leaderboardselect9:
                            activity.putExtra("leaderboard", 9);
                            break;
                        case R.id.leaderboardselect10:
                            activity.putExtra("leaderboard", 10);
                            break;
                        case R.id.leaderboardselect11:
                            activity.putExtra("leaderboard", 11);
                            break;
                        case R.id.leaderboardselect12:
                            activity.putExtra("leaderboard", 12);
                            break;
                    }
                    startActivity(activity);
                    finish();
                }
            });
        }
    }

    private void backButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButtonLeaderboardSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
