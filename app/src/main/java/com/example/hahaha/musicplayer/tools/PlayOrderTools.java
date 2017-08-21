package com.example.hahaha.musicplayer.tools;

import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.model.enumeration.PlayOrder;

public class PlayOrderTools {
  public static int getDrawableRes(int playOrder) {
    if (playOrder == PlayOrder.NORMAL) return R.drawable.play_order_normal;
    if (playOrder == PlayOrder.RANDOM) return R.drawable.play_order_random;
    if (playOrder == PlayOrder.SINGLE) return R.drawable.play_order_single;
    throw new IllegalStateException("play order :"+ String.valueOf(playOrder)+" invalid");
  }
}
