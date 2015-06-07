package com.example.android.onthespot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void resetScores() {
        Button button = (Button) findViewById(R.id.resetScores);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Options.this, "Your scores have been reset", Toast.LENGTH_LONG).show();
                for (int i = 1; i < 11; i++) {
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
                for (int i = 1; i < 11; i++) {
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
                for (int i = 1; i < 11; i++) {
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
                Intent activity = new Intent(Options.this, MenuActivity.class);
                startActivity(activity);
                finish();
                //change to menu.class once integrated
            }
        });
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_options, menu);
//        return true;
//    }
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
