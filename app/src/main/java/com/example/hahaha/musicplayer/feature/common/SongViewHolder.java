package com.example.hahaha.musicplayer.feature.common;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.feature.base.BaseViewHolder;
import com.example.hahaha.musicplayer.model.entity.internal.Song;

public class SongViewHolder extends BaseViewHolder<Song> {
  @BindView(R.id.txt_song_name) TextView mTxtSongName;
  @BindView(R.id.txt_artist_album) TextView mTxtArtistAlbum;

  public SongViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_song);

  }

  @Override public void bindTo(int position, Song song) {
    mTxtSongName.setText(song.getName());
    mTxtArtistAlbum.setText(
        MusicApp.appResources().getString(R.string.song_artist_album, song.getArtist(), song.getAlbum()));
  }
}
