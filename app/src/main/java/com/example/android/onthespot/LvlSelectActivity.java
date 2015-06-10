package com.example.android.onthespot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class LvlSelectActivity extends Activity {
    String scoreKey, unlockKey, levelScore, levelNumber, levelImage;
    int i;
    SharedPreferences prefs;
    FullMenu musicClass = new FullMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_lvl_select);

        if (musicClass.playing == false) {
            musicClass.mpPlayer = musicClass.createMusic().create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        } else { }

        try {
            setupButtons();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        switchLvlButton1();
        switchLvlButton2();
        backButton();
    }

    private void setupButtons() throws IOException, JSONException {
        for (i = 1; i < 13; i++) {
            scoreKey = "level" + i + "score";
            unlockKey = "level" + i + "unlock";
            levelImage = "level" + i + "Button";

            prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);

            if ((prefs.getBoolean(unlockKey, false) || i == 1) && i < 12) {
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
                        musicClass.mpPlayer.release();
                        musicClass.playing = false;
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
                            case R.id.level7Button:
                                activity.putExtra("level", 6);
                                break;
                            case R.id.level8Button:
                                activity.putExtra("level", 7);
                                break;
                            case R.id.level9Button:
                                activity.putExtra("level", 8);
                                break;
                            case R.id.level10Button:
                                activity.putExtra("level", 9);
                                break;
                            case R.id.level11Button:
                                activity.putExtra("level", 10);
                                break;
                            case R.id.level12Button:
                                activity.putExtra("level", 11);
                                break;
                        }
                        startActivity(activity);
                        finish();
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
    private void switchLvlButton1() {
        Button button = (Button) findViewById(R.id.switchlvlbutton1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GridLayout grid1 = (GridLayout) findViewById(R.id.gridLvlSelect1);
                final GridLayout grid2 = (GridLayout) findViewById(R.id.gridLvlSelect2);

                grid2.animate().setDuration(0
                ).scaleX(1).scaleY(1).translationX(0).translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        grid2.setTranslationX(+1 * grid2.getWidth());
                        grid2.animate().setDuration(500).translationX(0).alpha(1);
                    }
                });
                grid1.setVisibility(View.INVISIBLE);
                grid2.setVisibility(View.VISIBLE);
            }
        });
    }
    private void switchLvlButton2() {
        Button button = (Button) findViewById(R.id.switchlvlbutton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GridLayout grid1 = (GridLayout) findViewById(R.id.gridLvlSelect1);
                final GridLayout grid2 = (GridLayout) findViewById(R.id.gridLvlSelect2);



                grid1.animate().setDuration(0
                ).scaleX(1).scaleY(1).translationX(0).translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        grid1.setTranslationX(-1 * grid1.getWidth());
                        grid1.animate().setDuration(500).translationX(0).alpha(1);
                    }
                });
                grid2.setVisibility(View.INVISIBLE);
                grid1.setVisibility(View.VISIBLE);
            }
        });
    }

    private void backButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButtonLvlSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
}
