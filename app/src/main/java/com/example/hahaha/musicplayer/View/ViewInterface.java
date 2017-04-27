package com.example.hahaha.musicplayer.View;

/**
 * Created by hahaha on 9/14/16.
 */
public interface ViewInterface {

    //notify that the song change to i which is in the given songList
    //the state is in the playInfo
    public void onSongChange();
    //notify that the specific song's uri go wrong
    public void onUriWrong(String songName);
}
