package com.example.hahaha.musicplayer.feature.main.bar;

import android.content.ServiceConnection;
import android.os.Bundle;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.widget.LoopView;
import com.example.hahaha.musicplayer.widget.Pause_PlayView;
import com.example.hahaha.musicplayer.widget.ScrollTextView;
import nucleus.factory.RequiresPresenter;
import starter.kit.app.StarterFragment;

@RequiresPresenter(ControllBarPresenter.class)
public class ControlBarFragment extends StarterFragment<ControllBarPresenter> {
  @BindView(R.id.view_pause_play) Pause_PlayView mViewPausePlay;
  @BindView(R.id.view_loop) LoopView mViewLoop;
  @BindView(R.id.txt_song_name) ScrollTextView mTxtSongName;

  private ServiceConnection mServiceConnection;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_controll_bar;
  }

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
  }


}
