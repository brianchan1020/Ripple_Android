package com.example.brianchan.ripple_android;

import android.view.View;

import com.android.volley.RequestQueue;
import com.spotify.sdk.android.player.SpotifyPlayer;

/**
 * Created by Parikshit on 2/26/17.
 */

public class Global {
    public static SpotifyPlayer player;
    public static RequestQueue httpRequestQueue;
    public static Party party;
    public static View rrootView;
    public static PlaylistThread nextSongThread;
    public static boolean firstTime = true;
}
