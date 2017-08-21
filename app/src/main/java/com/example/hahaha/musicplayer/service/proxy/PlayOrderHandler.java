package com.example.hahaha.musicplayer.service.proxy;

import android.support.v4.util.SparseArrayCompat;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.model.enumeration.PlayOrder;
import java.util.List;

abstract class PlayOrderHandler {

  abstract int getOrder();
  abstract int next(int currentIndex, List<Song> songList);
  abstract int prev(int currentIndex, List<Song> songList);

  private static SparseArrayCompat<PlayOrderHandler> sMap = new SparseArrayCompat<>();

  static PlayOrderHandler getHandler(int playOrder) {
    PlayOrderHandler result = sMap.get(playOrder);
    if (result != null) return result;
    result = generate(playOrder);
    sMap.put(playOrder, result);
    return result;
  }

  private static PlayOrderHandler generate(int playOrder) {
    if (playOrder == PlayOrder.NORMAL) return new NormalHandler();
    if (playOrder == PlayOrder.SINGLE) return new SingleHandler();
    if (playOrder == PlayOrder.RANDOM) return new RandomHandler();
    throw new IllegalStateException(
        "playOrder:" + String.valueOf(playOrder)+" has no handler !");
  }

  private static class NormalHandler extends PlayOrderHandler {
    @Override int getOrder() {
      return PlayOrder.NORMAL;
    }

    @Override int next(int currentIndex, List<Song> songList) {
      ++currentIndex;
      if (currentIndex >= songList.size()) return 0;
      return currentIndex;
    }

    @Override int prev(int currentIndex, List<Song> songList) {
      --currentIndex;
      if (currentIndex < 0) return songList.size() - 1;
      return currentIndex;
    }
  }

  private static class SingleHandler extends PlayOrderHandler {
    @Override int getOrder() {
      return PlayOrder.SINGLE;
    }

    @Override int next(int currentIndex, List<Song> songList) {
      return currentIndex;
    }

    @Override int prev(int currentIndex, List<Song> songList) {
      return currentIndex;
    }
  }

  private static class RandomHandler extends PlayOrderHandler {
    @Override int getOrder() {
      return PlayOrder.RANDOM;
    }

    @Override int next(int currentIndex, List<Song> songList) {
      return (int) (Math.random() * songList.size());
    }

    @Override int prev(int currentIndex, List<Song> songList) {
      return (int) (Math.random() * songList.size());
    }
  }
}
