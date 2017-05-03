package com.example.hahaha.musicplayer.feature.lrc.seek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(SeekBarPresenter.class)
public class SeekBarFragment extends BaseFragment<SeekBarPresenter> {
  @BindView(R.id.seekBar) SeekBar mSeekBar;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_seekbar;
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

  void setProgress(int progress) {
    mSeekBar.setProgress(progress);
  }
}
