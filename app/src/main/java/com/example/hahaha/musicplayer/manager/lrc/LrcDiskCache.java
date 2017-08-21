package com.example.hahaha.musicplayer.manager.lrc;

import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import com.example.hahaha.musicplayer.tools.FileTools;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.tools.StreamTools;
import com.jakewharton.disklrucache.DiskLruCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import starter.kit.util.Md5Util;

class LrcDiskCache {
  private static final String CACHE_DIR_NAME = "lrc";
  private static final long MAX_SIZE = 5 * 1024 * 1024;
  private DiskLruCache mDiskCache;

  void open() {
    if (mDiskCache != null) return;
    try {
      File dir = FileTools.getDiskCacheDir(MusicApp.appContext(), CACHE_DIR_NAME);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      mDiskCache = DiskLruCache.open(dir, 1, 1, MAX_SIZE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void close() {
    if (mDiskCache == null) return;
    try {
      mDiskCache.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    mDiskCache = null;
  }

  void put(String name, InputStream inputStream) {
    if (mDiskCache == null) return;
    String key = Md5Util.md5(name);
    try {
      DiskLruCache.Editor editor = mDiskCache.edit(key);
      if (editor == null) return;
      if (StreamTools.inputToOutput(inputStream, editor.newOutputStream(0))) {
        //success
        editor.commit();
      } else {
        //fail
        editor.abort();
      }
      mDiskCache.flush();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  List<LrcLineInfo> get(String name) {
    if (mDiskCache == null) return null;
    String key = Md5Util.md5(name);
    InputStream inputStream = null;
    try {
      DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
      if (snapshot == null) return null;
      inputStream = snapshot.getInputStream(0);
      return LrcTools.toLrcList(inputStream);

    } catch (Exception e) {
      e.printStackTrace();

    } finally {
      try {
        if (inputStream != null) inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
