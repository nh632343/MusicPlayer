package com.example.hahaha.musicplayer.app;

public interface Navigator {
  int MUSIC_NOTI_ID = 20;
  String EXTRA_SONG_LIST = "EXTRA_SONG_LIST";
  String EXTRA_SONG_LIST_TYPE = "EXTRA_SONG_LIST_TYPE";
  String EXTRA_SONG_INDEX = "EXTRA_SONG_INDEX";

  int GET_SONG_LIST = 1;
  String DISMISS = "DISMISS";
  String PREVIOUS_SONG = "PREVIOUS_SONG";
  String NEXT_SONG = "NEXT_SONG";
  String PLAY_PAUSE = "PLAY_PAUSE";
  String NOTI_CLICK = "NOTI_CLICK";
  String PLAY_SONG = "PLAY_SONG";
  String PREPARE_IF_FIRST_TIME = "PREPARE_IF_FIRST_TIME";
}
