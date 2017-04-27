package com.example.hahaha.musicplayer.feature.main.list;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.model.entity.Song;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MusicListHandler extends Handler {
  public static void replySongList(ArrayList<Song> songList, Messenger clientMessenger) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(Navigator.EXTRA_SONG_LIST, songList);
    Message replyMsg = Message.obtain();
    replyMsg.setData(bundle);
    replyMsg.what = Navigator.GET_SONG_LIST;
    try {
      clientMessenger.send(replyMsg);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private WeakReference<MusicListPresenter> mWeakRef;

  public MusicListHandler(MusicListPresenter presenter) {
    mWeakRef = new WeakReference<>(presenter);
  }

  @Override public void handleMessage(Message msg) {
    MusicListPresenter presenter = mWeakRef.get();
    if (presenter == null) return;
    switch (msg.what) {
      case Navigator.GET_SONG_LIST:
        ArrayList<Song> songList = msg.getData().getParcelableArrayList(Navigator.EXTRA_SONG_LIST);
        if (songList == null) {
          presenter.getSongListError();
          return;
        }
        presenter.onGetSongList(songList);
        return;
      default:
        super.handleMessage(msg);
    }
  }
}
