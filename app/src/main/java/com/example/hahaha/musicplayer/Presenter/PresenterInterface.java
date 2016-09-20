package com.example.hahaha.musicplayer.Presenter;

import com.example.hahaha.musicplayer.Info.PlayInfo;
import com.example.hahaha.musicplayer.Info.SongInfo;
import com.example.hahaha.musicplayer.View.LrcInterface;
import com.example.hahaha.musicplayer.View.ViewInterface;

import java.util.List;

/**
 * Created by hahaha on 9/14/16.
 */
public interface PresenterInterface {
    //play the specific song but when method setDataSource failed, mediaPlayer will play the next song
    //return false for request focus failed
    //return true for request focus success
    public boolean play(int i,List<SongInfo> songList);

    //call to pause
    public void pause();

    //call to start
    public void start();

    //set loop
    public void setLoop(boolean loop);
    public void setProgress(int progress);

    public void finish();

    public void addViewListener(ViewInterface viewInterface);
    public void removeViewListener(ViewInterface viewInterface);

    public void setLrcListener(LrcInterface lrcInterface);
    public void removeLrcListener(LrcInterface lrcInterface);

    //get method
    public String getSongName();
    public boolean isStart();
    public boolean isLoop();
    public List<SongInfo> getCurrentList();

    public int getDuration();
}
