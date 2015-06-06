package com.example.android.onthespot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.onthespot.R;

public class LvlSelectActivity extends ActionBarActivity {
    String scoreKey, unlockKey, levelScore, levelNumber, levelImage;
    int i;
    SharedPreferences prefs;

//    //disables the default android backbutton
//    @Override
//    public void onBackPressed() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl_select);

        setupButtons();
    }

    private void setupButtons() {
        for (i = 1; i < 7; i++) {
            scoreKey = "level" + i + "score";
            unlockKey = "level" + i + "unlock";
            levelImage = "level" + i + "Button";

            prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);

            if ((prefs.getBoolean(unlockKey, false) || i == 1) && i < 6) {
                levelScore = "level" + i + "Score";
                levelNumber = "levelText" + i;

                int scoreId = this.getResources().getIdentifier(levelScore, "id", this.getPackageName());
                TextView tHighScore = (TextView) findViewById(scoreId);
                tHighScore.setText("" + prefs.getInt(scoreKey, 0));

                int levelNumberId = getResources().getIdentifier(levelNumber, "id", this.getPackageName());
                TextView tLevelNumber = (TextView) findViewById(levelNumberId);
                tLevelNumber.setText("" + i);

                int imageButtonId = getResources().getIdentifier(levelImage, "id", this.getPackageName());
                ImageButton tLevelImage = (ImageButton) findViewById(imageButtonId);
                tLevelImage.setImageResource(R.drawable.star0);
                tLevelImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

                tLevelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(i);
                        Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                        switch(v.getId()) {
                            case R.id.level1Button:
                                activity.putExtra("level", 0);
                                break;
                            case R.id.level2Button:
                                activity.putExtra("level", 1);
                                break;
                            case R.id.level3Button:
                                activity.putExtra("level", 2);
                                break;
                            case R.id.level4Button:
                                activity.putExtra("level", 3);
                                break;
                            case R.id.level5Button:
                                activity.putExtra("level", 4);
                                break;
                            case R.id.level6Button:
                                activity.putExtra("level", 5);
                                break;
                        }
                        startActivity(activity);
                    }
                });
            }

            else {
                int imageButtonId = getResources().getIdentifier(levelImage, "id", this.getPackageName());
                ImageButton tLevelImage = (ImageButton) findViewById(imageButtonId);
                tLevelImage.setImageResource(R.drawable.levellocked);
                tLevelImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }

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
