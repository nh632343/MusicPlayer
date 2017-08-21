package com.example.hahaha.musicplayer.feature.play.info;

import android.widget.TextView;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.model.entity.api.DoubanSong;

public class BriefInfoFragment extends BaseFragment {
  public static BriefInfoFragment newInstance() {
    return new BriefInfoFragment();
  }

  @BindView(R.id.txt_artist) TextView txtArtist;
  @BindView(R.id.txt_albun) TextView txtAlbun;
  @BindView(R.id.txt_rate) TextView txtRate;
  @BindView(R.id.txt_rate_num) TextView txtRateNum;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_song_brief_info;
  }

  public void setContent(DoubanSong doubanSong) {
    txtArtist.setText(doubanSong.getAuthor().getName());
    txtAlbun.setText(doubanSong.getAlbumTitle());
    txtRate.setText(doubanSong.getRating().getAverage());
    txtRateNum.setText(
        String.valueOf(doubanSong.getRating().getNumRaters()));
  }

}
