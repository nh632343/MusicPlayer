package com.example.hahaha.musicplayer.service.proxy;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.model.entity.internal.PlayStateInfo;
import com.example.hahaha.musicplayer.model.entity.internal.PositionInfo;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.model.entity.internal.SongRecord;
import com.example.hahaha.musicplayer.model.enumeration.PlayOrder;
import com.example.hahaha.musicplayer.service.ServiceMessageHelper;
import com.example.hahaha.musicplayer.service.aid.IMusicManager;
import com.example.hahaha.musicplayer.service.aid.PositionListener;
import com.example.hahaha.musicplayer.service.aid.SongChangeListener;
import com.example.hahaha.musicplayer.widget.ComObservable;
import com.example.hahaha.musicplayer.widget.ComSubscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;
import rx.functions.Action0;

public class MusicManagerProxy {
  private static class SingletonHolder {
    static final MusicManagerProxy singleton = new MusicManagerProxy();
  }

  public static MusicManagerProxy getInstance() {
    return SingletonHolder.singleton;
  }

  private SaveDataHelper mSaveDataHelper;

  private ServiceConnection mServiceConn;
  private WeakReference<Action0> mInitCallback;
  private IMusicManager mMusicManager;

  private PositionListener.Stub mPostionStub;
  private ComObservable<PositionInfo> mPositionObservable;
  private PositionInfo mLastPositionInfo;

  private SongChangeListener.Stub mSongChangeStub;
  private ComObservable<PlayStateInfo> mSongChangeObservable;
  private PlayStateInfo mLastPlayStateInfo;
  private SongListManager mSongListManager;

  private MusicManagerProxy() {
    mLastPositionInfo = new PositionInfo(0, 1);
    mSaveDataHelper = new SaveDataHelper();
    mServiceConn = new ServiceConnection() {
      @Override public void onServiceConnected(ComponentName name, IBinder service) {
        connected(service);
      }

      @Override public void onServiceDisconnected(ComponentName name) {
        mMusicManager = null;
      }
    };

    mPositionObservable = new ComObservable<>();
    mPostionStub = new PositionListener.Stub() {
      @Override public void onPositionChange(PositionInfo positionInfo) throws RemoteException {
        if (positionInfo.getCurrentTime() < 0 || positionInfo.getDuration() < 1) return;
        mLastPositionInfo = positionInfo;
        mPositionObservable.broadcast(mLastPositionInfo);
      }
    };

    mSongListManager = new SongListManager();
    mSongChangeObservable = new ComObservable<>();
    mSongChangeStub = new SongChangeListener.Stub() {
      @Override public void onError() throws RemoteException {
        playNext();
      }

      @Override public void onComplete() throws RemoteException {
        playNext();
      }

      @Override public void onStateChange(boolean play) throws RemoteException {
        if (mLastPlayStateInfo == null) return;
        mLastPlayStateInfo = new PlayStateInfo(
            mLastPlayStateInfo.getSong(), play, mSongListManager.getPlayOrder());
        mSongChangeObservable.broadcast(mLastPlayStateInfo);
      }
    };
  }

  public void initAsync(Action0 callback) {
    if (mMusicManager != null) {
      callback.call();
    }

    mInitCallback = new WeakReference<Action0>(callback);
    MusicApp.appContext().startService(ServiceMessageHelper.createIntent());
    MusicApp.appContext().bindService(ServiceMessageHelper.createIntent()
        , mServiceConn, Context.BIND_AUTO_CREATE);
  }

  private void connected(IBinder service) {
    mMusicManager = IMusicManager.Stub.asInterface(service);
    try {
      mMusicManager.registerPositionListener(mPostionStub);
      mMusicManager.registerSongChangeListener(mSongChangeStub);
    } catch (RemoteException e) {
      e.printStackTrace();
    }

    mSaveDataHelper.restore();

    Action0 callback = mInitCallback == null ? null : mInitCallback.get();
    if (callback == null) return;
    mInitCallback = null;
    callback.call();
  }

  public void exit() {
    if (mMusicManager != null) {
      try {
        mMusicManager.unregisterPositionListener(mPostionStub);
        mMusicManager.unregisterSongChangeListener(mSongChangeStub);
      } catch (Exception e) {e.printStackTrace();}
    }
    mMusicManager = null;

    try {
      MusicApp.appContext().unbindService(mServiceConn);
      MusicApp.appContext().stopService(ServiceMessageHelper.createIntent());
    } catch (Exception e) {e.printStackTrace();}
  }

  public void setCurrentSong(long songId, List<Song> songList, boolean shouldPlay) {
    if (songList == null || songList.isEmpty())
      throw new NullPointerException("songList can not be empty!!");
    mSaveDataHelper.saveSongList(songList);
    mSaveDataHelper.saveSongId(songId);
    Song song = mSongListManager.setCurrentSong(songId, songList);
    setSong(song, shouldPlay);
  }

  public void setCurrentSong(long songId, boolean shouldPlay) {
    mSaveDataHelper.saveSongId(songId);
    mSongListManager.setCurrentSong(songId);
    Song song = mSongListManager.getCurrentSong();
    if (song == null) return;
    setSong(song, shouldPlay);
  }

  private void setSong(Song song, boolean shouldPlay) {
    if (mMusicManager == null) return;
    if (song == null) return;
    try {
      mMusicManager.setSong(song, shouldPlay);
    } catch (Exception e) {e.printStackTrace();}
    mLastPlayStateInfo = new PlayStateInfo(song, shouldPlay, mSongListManager.getPlayOrder());
    mSongChangeObservable.broadcast(mLastPlayStateInfo);
  }

  public void playNext() {
    if (mMusicManager == null) return;
    setSong(mSongListManager.nextSong(), true);
  }

  public void playPrevious() {
    if (mMusicManager == null) return;
    setSong(mSongListManager.prevSong(), true);
  }

  public void playOrPause() {
    if (mMusicManager == null) return;
    ServiceMessageHelper.playOrPause();
  }

  public void setPlayOrder(@PlayOrder int playOrder) {
    mSongListManager.setPlayOrder(playOrder);
    if (mLastPlayStateInfo == null) return;
    mLastPlayStateInfo = new PlayStateInfo(
        mLastPlayStateInfo.getSong(), mLastPlayStateInfo.isPlaying(), playOrder);
    mSongChangeObservable.broadcast(mLastPlayStateInfo);
  }

  public void setPosition(int position) {
    if (mMusicManager == null) return;
    ServiceMessageHelper.setPosition(position);
  }

  public List<Song> getCurrentList() {
    return mSongListManager.getCurrentList();
  }

  public void addPositionListener(ComSubscriber<PositionInfo> subscriber) {
    mPositionObservable.addSubscriber(subscriber);
    subscriber.onChange(mLastPositionInfo);
  }

  public void removePositionListener(ComSubscriber<PositionInfo> subscriber) {
    mPositionObservable.removeSubscriber(subscriber);
  }

  public void addSongChangeListener(ComSubscriber<PlayStateInfo> subscriber) {
    mSongChangeObservable.addSubscriber(subscriber);
    subscriber.onChange(mLastPlayStateInfo);
  }

  public void removeSongChangeListener(ComSubscriber<PlayStateInfo> subscriber) {
    mSongChangeObservable.removeSubscriber(subscriber);
  }

  public String getCurrentSongName() {
    if (mLastPlayStateInfo == null) return "";
    return mLastPlayStateInfo.getSong().getName();
  }

  private class SaveDataHelper {
    SharedPreferences mSharedPreferences;

    SaveDataHelper() {
      mSharedPreferences = MusicApp.appContext().getSharedPreferences(
          Navigator.SP_CURRENT_SONG_LIST, Context.MODE_PRIVATE);
    }

    void saveSongId(long id) {
      SharedPreferences.Editor editor = mSharedPreferences.edit();
      editor.putLong(Navigator.KEY_SONG_ID, id);
      editor.apply();
    }

    void saveSongList(List<Song> songList) {
      DataSupport.deleteAll(SongRecord.class);
      for (Song song : songList) {
        new SongRecord(song.getId()).save();
      }
    }

    void restore() {
      List<SongRecord> songRecordList = DataSupport.findAll(SongRecord.class);
      if (songRecordList.isEmpty()) return;
      ArrayList<Song> songList = new ArrayList<>();
      for (SongRecord songRecord : songRecordList) {
        Song song = songRecord.getSong();
        if (song == null) continue;
        songList.add(song);
      }
      if (songList.isEmpty()) return;
      long songId = mSharedPreferences.getLong(Navigator.KEY_SONG_ID, 0);
      setCurrentSong(songId, songList, false);
    }
  }
}
