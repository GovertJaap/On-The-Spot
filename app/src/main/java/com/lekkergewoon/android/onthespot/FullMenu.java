package com.lekkergewoon.android.onthespot;

import android.media.MediaPlayer;

/**
 * Created by Mike on 10-6-2015.
 */
public class FullMenu {
    public static MediaPlayer mpPlayer;
    public static boolean exit;
    public static boolean playing = false;
    public static boolean musicOn = true;
    public static int length;

    public FullMenu() {

    }

    public MediaPlayer createMusic() {
        return mpPlayer;
    }
}
