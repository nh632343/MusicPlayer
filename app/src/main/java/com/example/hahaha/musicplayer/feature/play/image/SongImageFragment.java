package com.example.hahaha.musicplayer.feature.play.image;

import android.widget.TextView;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSong;
import com.example.hahaha.musicplayer.widget.TwoSideSwipeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.utilities.ViewUtils;

public class SongImageFragment extends BaseFragment
  implements TwoSideSwipeLayout.SwipeListener {
  public static SongImageFragment newInstance() {
    return new SongImageFragment();
  }

  @BindView(R.id.txt_artist) TextView mTxtArtist;
  @BindView(R.id.img_song) SimpleDraweeView mImgSong;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_song_image;
  }

  public void setContent(DoubanSong doubanSong) {
    mTxtArtist.setText(doubanSong.getAuthor().getName());
    mImgSong.setImageURI(doubanSong.getImageUrl());
  }

  @Override public void onSwipe(boolean isLeftSwipe, float percent) {
    if (percent > 0.9) {
      ViewUtils.setInvisible(getView(), true);
      return;
    }
    ViewUtils.setInvisible(getView(), false);
    float alpha = Math.min(Math.max(0, 1- percent), 1);
    getView().setAlpha(alpha);
  }
}
