package com.example.android.onthespot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameOver extends ActionBarActivity {

    TextView scoreTextView, highScoreTextView;
    //disables the default android backbutton
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        scoreTextView= (TextView) findViewById(R.id.textViewScore);
        highScoreTextView = (TextView) findViewById(R.id.textViewHighscore);
        //MenuButton();
        //ScoreButton();
        load();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void load ()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("MyScores", Context.MODE_PRIVATE);
        Integer HighScore=sharedPreferences.getInt("HighScore", 20);

        highScoreTextView.setText(HighScore);

        Bundle extras = getIntent().getExtras();
        int scores = extras.getInt("scores", 0);

        scoreTextView.setText(scores);
    }

    private void MenuButton() {
        Button button = (Button) findViewById(R.id.GotoMenu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOver.this, MenuActivity.class));
                //change to menu.class once integrated
            }
        });
    }


    private void ScoreButton() {
        //Button button = (Button) findViewById(R.id.textViewScore);
        //button.setOnClickListener(new View.OnClickListener() {
            //public void onClick(View v) {
                //Toast.makeText(GameOver.this, "Leaderboards are not available.", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(GameOver.this, MainActivity.class));
                //change to leaderboards.class once integrated
            //}
        };


    @Override
    public boolean onTouchEvent(MotionEvent e){
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            Intent activity = new Intent(GameOver.this, MainActivity.class);
            activity.putExtra("level", getIntent().getExtras().getInt("level"));
            startActivity(activity);
        }
        return true;
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_over, menu);
        return true;
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
    } */
}
