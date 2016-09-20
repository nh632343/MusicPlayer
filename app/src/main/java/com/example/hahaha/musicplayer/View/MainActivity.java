package com.example.hahaha.musicplayer.View;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hahaha.musicplayer.MyLog;
import com.example.hahaha.musicplayer.Presenter.MusicService;
import com.example.hahaha.musicplayer.Info.PlayInfo;
import com.example.hahaha.musicplayer.Presenter.PresenterInterface;
import com.example.hahaha.musicplayer.Info.SongInfo;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.SelfMadeVIew.LoopView;
import com.example.hahaha.musicplayer.SelfMadeVIew.Pause_PlayView;
import com.example.hahaha.musicplayer.SelfMadeVIew.ScrollTextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewInterface{


    private PresenterInterface presenter;
    private List<SongInfo> songInfoList;
    private ServiceConnection serviceConnection=
            new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                    presenter=((MusicService.MyBinder)service).getService().getPresenter();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

    private ImageView frontPic;
    private TextView noSongTv1;
    private TextView noSongTv2;

    private ScrollTextView songTv;
    private LoopView loopView;
    private Pause_PlayView p_pView;
    private ListView listView;

    private boolean isOver=false;

    private class CheckTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            while (presenter==null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            songInfoList=presenter.getCurrentList();
            if (songInfoList.size()==0){
                //no song
                noSongTv1.setVisibility(View.VISIBLE);
                noSongTv2.setVisibility(View.VISIBLE);

                return;
            }
            //have songs

            listView.setAdapter(new ListViewAdapter(MainActivity.this,R.layout.listview_item
                                  ,songInfoList));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    presenter.play(position,songInfoList);
                }
            });

            songTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,LrcActivity.class));
                }
            });

            presenter.addViewListener(MainActivity.this);

            frontPic.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection,BIND_AUTO_CREATE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        frontPic= (ImageView) findViewById(R.id.frontPic);
        noSongTv1= (TextView) findViewById(R.id.noSongTv1);
        noSongTv2= (TextView) findViewById(R.id.noSongTv2);
        songTv= (ScrollTextView) findViewById(R.id.songTv);
        loopView= (LoopView) findViewById(R.id.loopVIew);
        loopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setLoop(((LoopView)v).getState());
            }
        });

        p_pView= (Pause_PlayView) findViewById(R.id.p_pView);
        p_pView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean start=((Pause_PlayView)v).getState();
                if (start){
                    presenter.start();
                }
                else{
                    presenter.pause();
                }
            }
        });
        listView= (ListView) findViewById(R.id.songListView);
        new CheckTask().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeViewListener(this);
        presenter=null;
        unbindService(serviceConnection);
        if (isOver){
            stopService(new Intent(MainActivity.this,MusicService.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_exit:
                isOver=true;

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSongChange() {
        songTv.setText(presenter.getSongName());
        loopView.setState(presenter.isLoop());
        p_pView.setState(presenter.isStart());
    }

    @Override
    public void onUriWrong(String songName) {
        Toast.makeText(MainActivity.this,songName+" failed",Toast.LENGTH_SHORT).show();
    }
}
