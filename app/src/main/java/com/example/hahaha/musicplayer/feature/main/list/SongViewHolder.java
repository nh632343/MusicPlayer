package com.example.hahaha.musicplayer.feature.main.list;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.model.entity.Song;
import support.ui.adapters.EasyViewHolder;

public class SongViewHolder extends EasyViewHolder<Song> {
  @BindView(R.id.txt_song_name) TextView mTxtSongName;

  public SongViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_song_name);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, Song value) {
    mTxtSongName.setText(value.name);
  }
}
