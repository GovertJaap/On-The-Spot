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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

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

        try {
            setupButtons();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setupButtons() throws IOException, JSONException {
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

                JSONObject obj = new JSONObject(loadJSONFromAsset());
                JSONArray jArray = obj.getJSONArray("main");
                JSONObject levelData = jArray.getJSONObject(i - 1);
                int star1Requirement = levelData.getInt("1-Star");
                int star2Requirement = levelData.getInt("2-Star");
                int star3Requirement = levelData.getInt("3-Star");

                if (prefs.getInt(scoreKey, 0) > star3Requirement) {
                    tLevelImage.setImageResource(R.drawable.star3);
                }
                else if (prefs.getInt(scoreKey, 0) > star2Requirement) {
                    tLevelImage.setImageResource(R.drawable.star2);
                }
                else if (prefs.getInt(scoreKey, 0) > star1Requirement) {
                    tLevelImage.setImageResource(R.drawable.star1);
                }
                else {
                    tLevelImage.setImageResource(R.drawable.star0);
                }
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
