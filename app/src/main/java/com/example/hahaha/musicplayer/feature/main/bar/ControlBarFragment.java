package com.example.hahaha.musicplayer.feature.main.bar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseContentFragment;
import com.example.hahaha.musicplayer.feature.service.interact.ServiceMessageHelper;
import com.example.hahaha.musicplayer.widget.LoopView;
import com.example.hahaha.musicplayer.widget.PausePlayView;
import com.example.hahaha.musicplayer.widget.ScrollTextView;
import nucleus.factory.RequiresPresenter;
import support.ui.content.RequiresContent;

@RequiresContent(loadView = ControlBarLoadView.class)
@RequiresPresenter(ControllBarPresenter.class)
public class ControlBarFragment extends BaseContentFragment<ControllBarPresenter> {
  @BindView(R.id.content) View mContentView;
  @BindView(R.id.view_pause_play) PausePlayView mViewPausePlay;
  @BindView(R.id.view_loop) LoopView mViewLoop;
  @BindView(R.id.txt_song_name) ScrollTextView mTxtSongName;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_controll_bar;
  }

  @Override public View provideContentView() {
    return mContentView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewPausePlay.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ServiceMessageHelper.playOrPause();
      }
    });
  }

  void setSongName(String songName) {
    mTxtSongName.setText(songName);
  }

  void setPlayState(boolean isPlaying) {
    mViewPausePlay.setState(isPlaying);
  }
}
