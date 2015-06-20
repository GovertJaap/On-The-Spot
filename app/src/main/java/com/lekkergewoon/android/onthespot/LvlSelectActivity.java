package com.lekkergewoon.android.onthespot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
    boolean transition;
    SharedPreferences prefs;
    FullMenu musicClass = new FullMenu();

    @Override
    public void onBackPressed() {
        transition = true;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_lvl_select);

        try {
            setupButtons();
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        if (musicClass.playing == false && musicClass.mpPlayer == null && musicClass.musicOn == true) {
            musicClass.mpPlayer = musicClass.createMusic().create(this, R.raw.menu);
            musicClass.mpPlayer.start();
            musicClass.playing = true;
        } else if (musicClass.playing == false && musicClass.mpPlayer != null && musicClass.musicOn == true) {
            musicClass.mpPlayer.start();
            musicClass.mpPlayer.seekTo(musicClass.length);
            musicClass.playing = true;
        }

        switchLvlButton1();
        switchLvlButton2();
        backButton();
    }

    @Override
    protected void onPause() {
        if (musicClass.mpPlayer != null && transition != true && musicClass.musicOn == true) {
            musicClass.mpPlayer.pause();
            musicClass.length = musicClass.mpPlayer.getCurrentPosition();
            musicClass.playing = false;
            musicClass.exit = true;
        }

        super.onPause();
    }

    @Override
    protected void onResume() {


        transition = false;
        super.onResume();
    }

    private void setupButtons() throws IOException, JSONException {
        for (i = 1; i < 13; i++) {
            scoreKey = "level" + i + "score";
            unlockKey = "level" + i + "unlock";
            levelImage = "level" + i + "Button";

            prefs = this.getSharedPreferences("mainLevelsSave", Context.MODE_PRIVATE);

            if ((prefs.getBoolean(unlockKey, false) || i == 1)) {
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
                        if (musicClass.musicOn == true) {
                            musicClass.mpPlayer.release();
                            musicClass.playing = false;
                        }

                        transition = true;
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
        ImageButton button = (ImageButton) findViewById(R.id.switchlvlbutton1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GridLayout grid1 = (GridLayout) findViewById(R.id.gridLvlSelect1);
                final GridLayout grid2 = (GridLayout) findViewById(R.id.gridLvlSelect2);

                grid2.animate().setDuration(0
                ).scaleX(1).scaleY(1).translationX(0).translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        grid1.setVisibility(View.INVISIBLE);
                        grid2.setVisibility(View.VISIBLE);
                        grid2.setTranslationX(+1 * grid2.getWidth());
                        grid2.animate().setDuration(500).translationX(0).alpha(1);
                    }
                });
            }
        });
    }
    private void switchLvlButton2() {
        ImageButton button = (ImageButton) findViewById(R.id.switchlvlbutton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GridLayout grid1 = (GridLayout) findViewById(R.id.gridLvlSelect1);
                final GridLayout grid2 = (GridLayout) findViewById(R.id.gridLvlSelect2);
                grid1.animate().setDuration(0
                ).scaleX(1).scaleY(1).translationX(0).translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        grid2.setVisibility(View.INVISIBLE);
                        grid1.setVisibility(View.VISIBLE);
                        grid1.setTranslationX(-1 * grid1.getWidth());
                        grid1.animate().setDuration(500).translationX(0).alpha(1);
                    }
                });
            }
        });
    }

    private void backButton() {
        ImageButton button = (ImageButton) findViewById(R.id.backButtonLvlSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition = true;
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
