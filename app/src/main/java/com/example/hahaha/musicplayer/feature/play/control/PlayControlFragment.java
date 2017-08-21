package com.example.hahaha.musicplayer.feature.play.control;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(PlayControlPresenter.class)
public class PlayControlFragment extends BaseFragment<PlayControlPresenter> {
  @BindView(R.id.txt_current_time) TextView mTxtCurrentTime;
  @BindView(R.id.txt_duration) TextView mTxtDuration;
  @BindView(R.id.seekBar) SeekBar mSeekBar;
  @BindView(R.id.view_play_order) View mViewPlayOrder;
  @BindView(R.id.view_play_pause) View mViewPlayPause;

  @OnClick(R.id.view_play_order) void changePlayOrder() {
    getPresenter().playOrderClick();
  }

  @OnClick(R.id.view_play_pause) void setPlayPause() {
    getPresenter().playPause();
  }

  @OnClick(R.id.view_prev) void setPrev() {
    getPresenter().prevSong();
  }

  @OnClick(R.id.view_next) void setNext() {
    getPresenter().nextSong();
  }

  @OnClick(R.id.view_song_list) void openSongList() {//TODO
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_play_control;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
      @Override public void onStartTrackingTouch(SeekBar seekBar) {}
      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        getPresenter().setProgress(seekBar.getProgress());
      }
    });
  }

  void setProgress(int progress, int currentTime, int duration) {
    mSeekBar.setProgress(progress);
    currentTime = currentTime / 1000;
    String current = MusicApp.appResources().getString(
        R.string.time_format, currentTime / 60, currentTime % 60);
    mTxtCurrentTime.setText(current);
    duration = duration / 1000;
    String dur = MusicApp.appResources().getString(
        R.string.time_format, duration / 60, duration % 60);
    mTxtDuration.setText(dur);
  }

  void setIsPlaying(boolean isPlaying) {
    if (isPlaying) {
      mViewPlayPause.setBackgroundResource(R.drawable.ic_pause);
      return;
    }
    mViewPlayPause.setBackgroundResource(R.drawable.ic_play);
  }

  void setPlayOrder(@DrawableRes int id) {
    mViewPlayOrder.setBackgroundResource(id);
  }
}
