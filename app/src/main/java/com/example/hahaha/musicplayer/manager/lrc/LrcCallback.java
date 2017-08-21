package com.example.hahaha.musicplayer.manager.lrc;

import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.util.List;

public interface LrcCallback {
  void lrcSuccess(List<List<LrcLineInfo>> result);
  void lrcFailure();
}
