package com.example.hahaha.musicplayer.View;

import com.example.hahaha.musicplayer.Info.PlayInfo;
import com.example.hahaha.musicplayer.Info.SongInfo;

import java.util.List;

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
