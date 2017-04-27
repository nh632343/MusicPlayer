package com.example.hahaha.musicplayer.feature.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.IBinder;
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

import com.example.hahaha.musicplayer.feature.service.MusicService;
import com.example.hahaha.musicplayer.Presenter.MusicPresenterInterface;
import com.example.hahaha.musicplayer.View.ListViewAdapter;
import com.example.hahaha.musicplayer.View.LrcActivity;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.widget.LoopView;
import com.example.hahaha.musicplayer.widget.Pause_PlayView;
import com.example.hahaha.musicplayer.widget.ScrollTextView;

import java.util.List;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MainPresenter.class)
public class FakeMainActivity extends BaseActivity<MainPresenter> {
  private MusicPresenterInterface presenter;
  private List<Song> songList;
  private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

          //presenter = ((MusicService.MyBinder) service).getService().getPresenter();
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

  private boolean isOver = false;

  private class CheckTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
      while (presenter == null) ;
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

      songList = presenter.getCurrentList();
      if (songList.size() == 0) {
        //no song
        noSongTv1.setVisibility(View.VISIBLE);
        noSongTv2.setVisibility(View.VISIBLE);

        return;
      }
      //have songs

      listView.setAdapter(new ListViewAdapter(FakeMainActivity.this, R.layout.list_song_name
          , songList));
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          presenter.play(position, songList);
        }
      });

      songTv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          startActivity(new Intent(FakeMainActivity.this, LrcActivity.class));
        }
      });

      presenter.addViewListener(null);

      frontPic.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_main_test);

    Intent intent = new Intent(FakeMainActivity.this, MusicService.class);
    startService(intent);
    bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    frontPic = (ImageView) findViewById(R.id.frontPic);
    noSongTv1 = (TextView) findViewById(R.id.noSongTv1);
    noSongTv2 = (TextView) findViewById(R.id.noSongTv2);
    songTv = (ScrollTextView) findViewById(R.id.songTv);
    loopView = (LoopView) findViewById(R.id.loopVIew);
    loopView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.setLoop(((LoopView) v).getState());
      }
    });

    p_pView = (Pause_PlayView) findViewById(R.id.p_pView);
    p_pView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean start = ((Pause_PlayView) v).getState();
        if (start) {
          presenter.start();
        } else {
          presenter.pause();
        }
      }
    });
    listView = (ListView) findViewById(R.id.songListView);
    new CheckTask().execute();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.removeViewListener(null);
    presenter = null;
    unbindService(serviceConnection);
    if (isOver) {
      stopService(new Intent(FakeMainActivity.this, MusicService.class));
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_exit:
        isOver = true;

        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }


  public void onSongChange() {
    songTv.setText(presenter.getCurrentSongName());
    loopView.setState(presenter.isLoop());
    p_pView.setState(presenter.isStart());
  }


  public void onUriWrong(String songName) {
    Toast.makeText(FakeMainActivity.this, songName + " failed", Toast.LENGTH_SHORT).show();
  }
}
