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
    }


    private void setupPlayButton() {
        ImageButton levelButton = (ImageButton) findViewById(R.id.level1Button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            ;
            @Override
            public void onClick(View v) {

                Intent activity = new Intent(LvlSelectActivity.this, MainActivity.class);
                activity.putExtra("level", 0);
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
