package com.example.hahaha.musicplayer.tools;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class FileTools {
  public static File getDiskCacheDir(Context context, String uniqueName) {
    String cachePath;
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()) {
      cachePath = context.getExternalCacheDir().getPath();
    } else {
      cachePath = context.getCacheDir().getPath();
    }
    return new File(cachePath + File.separator + uniqueName);
  }
}
