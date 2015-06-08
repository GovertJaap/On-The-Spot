package com.example.android.onthespot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GameOver extends Activity {
    TextView tScore, tHighScore;
    int score, highscore, levelNumber;
    String scoreKey;
    SharedPreferences prefs;

//    //disables the default android backbutton
//    @Override
//    public void onBackPressed() {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_game_over);

        updateScore();
        try {
            showScore();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        MenuButton();
        ScoreButton();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void updateScore() {
        score = getIntent().getExtras().getInt("score");
        //score = score - 5000;
        if (score < 0) {
            score = 0;
        }

        levelNumber = getIntent().getExtras().getInt("level");
        scoreKey = "level" + levelNumber + "score";

        prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = prefs.edit();
        highscore = prefs.getInt(scoreKey, 0); //0 is the default value

        if (score > highscore) {
            highscore = score;
            editor.putInt(scoreKey, highscore);
            editor.commit();
        }
    }

    private void showScore() throws IOException, JSONException {
        tScore = (TextView) findViewById(R.id.scoreGameOver);
        tScore.setText("Score: " + score);
        tHighScore = (TextView) findViewById(R.id.highscoreGameOver);
        tHighScore.setText("Highscore: " + prefs.getInt(scoreKey, 0));

        ImageView tLevelImage = (ImageView) findViewById(R.id.starViewGameOver);

        JSONObject obj = new JSONObject(loadJSONFromAsset());
        JSONArray jArray = obj.getJSONArray("main");
        JSONObject levelData = jArray.getJSONObject(levelNumber-1);
        int star1Requirement = levelData.getInt("1-Star");
        int star2Requirement = levelData.getInt("2-Star");
        int star3Requirement = levelData.getInt("3-Star");

        if (prefs.getInt(scoreKey, 0) > star3Requirement) {
            tLevelImage.setImageResource(R.drawable.stars_3);
        }
        else if (prefs.getInt(scoreKey, 0) > star2Requirement) {
            tLevelImage.setImageResource(R.drawable.stars_2);
        }
        else if (prefs.getInt(scoreKey, 0) > star1Requirement) {
            tLevelImage.setImageResource(R.drawable.stars_1);
        }
        else {
            tLevelImage.setImageResource(R.drawable.stars_0);
        }
        tLevelImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void MenuButton() {
        Button button = (Button) findViewById(R.id.GotoMenuGameOver);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent activity = new Intent(GameOver.this, MenuActivity.class);
//                startActivity(activity);
                finish();
                //change to menu.class once integrated
            }
        });
    }

    private void ScoreButton() {
        Button button = (Button) findViewById(R.id.Leaderboards);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameOver.this, "Leaderboards are not available.", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(GameOver.this, MainActivity.class));
                //change to leaderboards.class once integrated
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            Intent activity = new Intent(GameOver.this, MainActivity.class);
            activity.putExtra("level", levelNumber - 1);
            startActivity(activity);
            finish();
        }
        return true;
    }

    public String loadJSONFromAsset() throws IOException {
        String json;
        try {
            InputStream is = getAssets().open("leveldata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }

        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
