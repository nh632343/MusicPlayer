package com.example.hahaha.musicplayer;

import android.util.Log;

import com.example.hahaha.musicplayer.SelfMadeVIew.ScrollTextView;

/**
 * Created by hahaha on 9/15/16.
 */
public class MyLog {
    private static final int LEVEL=3;
    private static final int DEBUG=3;

    public static void d(String tag, String text){
        if (LEVEL==DEBUG){
        Log.d(tag,text);}
    }
}
