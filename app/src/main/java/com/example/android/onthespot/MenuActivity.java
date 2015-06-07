package com.example.android.onthespot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.onthespot.R;

public class MenuActivity extends Activity {
    //disables the default android backbutton
//    @Override
//    public void onBackPressed() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_menu);

        setupPlayButton();
        scoreButton();
        optionsButton();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void setupPlayButton() {
        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {

                Intent activity = new Intent(MenuActivity.this, LvlSelectActivity.class);
                //activity.putExtra("level", 0);
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
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
