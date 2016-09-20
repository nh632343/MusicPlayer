package com.example.hahaha.musicplayer.Presenter;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {

    private PresenterInterface presenter=new MusicPresenter();

    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    public PresenterInterface getPresenter(){
        return presenter;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.finish();
        presenter=null;
        Log.d("xyz","service destroy");
    }
}

