package com.example.hahaha.musicplayer.manager.lrc;

import android.support.v4.util.LruCache;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.util.List;

class LrcLruCache extends LruCache<String, List<LrcLineInfo>> {
  private static final int MAX_SIZE = 10;

  LrcLruCache() {
    super(MAX_SIZE);
  }

  @Override protected int sizeOf(String key, List<LrcLineInfo> value) {
    return 1;
  }
}
