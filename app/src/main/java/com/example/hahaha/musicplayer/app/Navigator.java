package com.example.hahaha.musicplayer.app;

public interface Navigator {
  int MUSIC_NOTI_ID = 20;
  String EXTRA_SONG_LIST = "EXTRA_SONG_LIST";
  String EXTRA_SONG_LIST_TYPE = "EXTRA_SONG_LIST_TYPE";
  String EXTRA_SONG_INDEX = "EXTRA_SONG_INDEX";
  String EXTRA_SONG_NAME = "EXTRA_SONG_NAME";
  String EXTRA_IS_PLAYING = "EXTRA_IS_PLAYING";

  // message what
  int GET_SONG_LIST = 1;
  int REGISTER_LISTENER = 2;
  int UN_REGISTER_LISTENER = 3;
  int ON_SONG_CHANGE = 4;
  int ON_PREPARING = 5;

  String DISMISS = "DISMISS";
  String PREVIOUS_SONG = "PREVIOUS_SONG";
  String NEXT_SONG = "NEXT_SONG";
  String PLAY_PAUSE = "PLAY_PAUSE";
  String NOTI_CLICK = "NOTI_CLICK";
  String PLAY_SONG = "PLAY_SONG";
  String PREPARE_IF_FIRST_TIME = "PREPARE_IF_FIRST_TIME";
}
