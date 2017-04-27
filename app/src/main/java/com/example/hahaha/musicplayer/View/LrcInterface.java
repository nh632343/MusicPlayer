package com.example.hahaha.musicplayer.View;

import com.example.hahaha.musicplayer.model.entity.LrcInfo;

/**
 * Created by hahaha on 9/17/16.
 */
public interface LrcInterface {
    void NoLrc(String songName);
    void onLrcAvailable(LrcInfo theLrcInfo);
    void onProgressChange(long time);
}
