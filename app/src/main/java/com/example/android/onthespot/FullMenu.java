package com.example.android.onthespot;

import android.media.MediaPlayer;

/**
 * Created by Mike on 10-6-2015.
 */
public class FullMenu {
    public static MediaPlayer mpPlayer;
    public static boolean playing = false;

    public FullMenu() {

    }

    public MediaPlayer createMusic() {
        return mpPlayer;
    }

}
