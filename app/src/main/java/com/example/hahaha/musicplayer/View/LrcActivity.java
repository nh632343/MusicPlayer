package com.example.hahaha.musicplayer.View;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hahaha.musicplayer.model.entity.LrcInfo;
import com.example.hahaha.musicplayer.feature.service.MusicService;
import com.example.hahaha.musicplayer.Presenter.MusicPresenterInterface;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.widget.LoopView;
import com.example.hahaha.musicplayer.widget.PausePlayView;
import com.example.hahaha.musicplayer.widget.ScrollLrcView;
import com.example.hahaha.musicplayer.widget.ScrollTextView;

public class LrcActivity extends AppCompatActivity implements ViewInterface,LrcInterface{

    private MusicPresenterInterface presenter;
    private ServiceConnection serviceConnection=
            new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    //presenter=((MusicService.MyBinder)service).getService().getPresenter();
                    presenter.addViewListener(LrcActivity.this);
                    presenter.setLrcListener(LrcActivity.this);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

    private ScrollTextView songTv;
    private LoopView loopView;
    private PausePlayView p_pView;
    private TextView durTv;
    private ScrollLrcView lrcView;
    private TextView searchTv;
    private TextView noLrcTv;
    private SeekBar mySeekBar;

    private String songName;
    private int myDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);

        Intent intent=new Intent(LrcActivity.this, MusicService.class);
        //startService(intent);
        bindService(intent, serviceConnection,BIND_AUTO_CREATE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        songTv= (ScrollTextView) findViewById(R.id.songTv);
        durTv= (TextView) findViewById(R.id.durTv);

        loopView= (LoopView) findViewById(R.id.loopVIew);
        loopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setLoop(((LoopView)v).getState());
            }
        });

        p_pView= (PausePlayView) findViewById(R.id.p_pView);
        p_pView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean start=((PausePlayView)v).getState();
                if (start){
                    presenter.start();
                }
                else{
                    presenter.pause();
                }
            }
        });
        lrcView= (ScrollLrcView) findViewById(R.id.lrcView);
        noLrcTv= (TextView) findViewById(R.id.noLrcTv);
        searchTv= (TextView) findViewById(R.id.searchTv);

        mySeekBar= (SeekBar) findViewById(R.id.seekBar);
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                   int progress=seekBar.getProgress()*myDuration/100;
                presenter.setProgress(progress);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeViewListener(this);
        presenter.removeLrcListener(this);
        presenter=null;
        unbindService(serviceConnection);

        }


    @Override
    public void NoLrc(String songName) {
        if (songName.equals(this.songName)){
            noLrcTv.setVisibility(View.VISIBLE);
            searchTv.setVisibility(View.GONE);
            lrcView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLrcAvailable(LrcInfo theLrcInfo) {
        if (theLrcInfo.name.equals(songName)){
            lrcView.setMyLrcInfo(theLrcInfo);
            searchTv.setVisibility(View.GONE);
            noLrcTv.setVisibility(View.GONE);
            lrcView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProgressChange(long time) {
        lrcView.setTime(time);
        int progress=(int) (time*100/myDuration);
        mySeekBar.setProgress(progress);
    }

    @Override
    public void onSongChange() {
        String temp=presenter.getCurrentSongName();

        //歌曲改变
        if (!temp.equals(songName)){
            noLrcTv.setVisibility(View.GONE);
            searchTv.setVisibility(View.VISIBLE);
            songName=temp;
            songTv.setText(songName);
            myDuration=presenter.getDuration();

            int min=myDuration/1000/60;
            int sec=myDuration/1000-min*60;
            durTv.setText(String.valueOf(min)+":"+String.valueOf(sec));
            lrcView.setMyLrcInfo(null);
        }

        loopView.setState(presenter.isLoop());
        p_pView.setState(presenter.isStart());
    }

    @Override
    public void onUriWrong(String songName) {
        Toast.makeText(LrcActivity.this,songName+" failed",Toast.LENGTH_SHORT).show();
    }
}
