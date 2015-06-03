package com.example.android.onthespot;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.android.onthespot.R;

public class LvlSelectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl_select);
        setupLevel1Button();
        setupLevel2Button();
        setupLevel3Button();
        setupLevel4Button();
        setupLevel5Button();
        setupLevel6Button();

    }


    private void setupLevel1Button() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level1Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 0);
                startActivity(activity);
            }
        });
    }

    private void setupLevel2Button() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level2Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 1);
                startActivity(activity);
            }
        });
    }
    private void setupLevel3Button() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level3Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 2);
                startActivity(activity);
            }
        });
    }
    private void setupLevel4Button() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level4Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 3);
                startActivity(activity);
            }
        });
    }
    private void setupLevel5Button() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level5Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 4);
                startActivity(activity);
            }
        });
    }
    private void setupLevel6Button() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level6Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 5);
                startActivity(activity);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
