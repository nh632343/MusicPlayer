package com.example.hahaha.musicplayer.tools;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class LrcTools {
  private static LrcComparator sLrcComparator = new LrcComparator();

  public static class BooleanHolder {
    volatile public boolean value;

    public BooleanHolder(boolean value) {
      this.value = value;
    }
  }

  /**
   * 通过歌词的时间比较
   */
  private static class LrcComparator implements Comparator<LrcLineInfo> {
    @Override
    public int compare(LrcLineInfo o1, LrcLineInfo o2) {
      return o1.getTime() > o2.getTime() ? 1 : -1;
    }
  }

  public static Observable<List<LrcLineInfo>> getLrcLineInfoList(String songName) {
    return getLrcLineInfoList(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), songName + ".lrc");
  }

  /**
   * 给定目录,歌词文件名(包括后缀),查找歌词文件, 返回InputStream
   * 在查找过程中会经常检查标志位
   * @param dir 需要查找的目录
   * @param fullSongName 歌词文件全称
   * @return 如果目录不合法或找不到或被终止,返回null
   */
  public static Observable<List<LrcLineInfo>> getLrcLineInfoList(@NonNull File dir, final String fullSongName) {
    Log.d("xyz", "search ditectory: " + dir.getName());
    Log.d("xyz", "search file: " + fullSongName);
    /*return Observable.just(dir)
                     .filter(new Func1<File, Boolean>() {
                       @Override public Boolean call(File file) {
                         if (!file.exists() || !file.isDirectory())
                           throw Exceptions.propagate(new Throwable("dir invalid"));
                         return false;}})
                     .flatMap(new Func1<File, Observable<File>>() {
                       @Override public Observable<File> call(@NonNull File file) {
                         Log.d("xyz", "listfiles: "+String.valueOf(file.listFiles().length));
                         return Observable.from(file.listFiles());}})
                     .filter(new Func1<File, Boolean>() {
                       @Override public Boolean call(@NonNull File file) {
                         Log.d("xyz", "file: " + file.getName());
                         return !file.getName().equals(fullSongName);}})
                     *//*.toList()
                     .map(new Func1<List<File>, File>() {
                       @Override public File call(List<File> files) {
                         if (files == null || files.size() == 0)
                         {Log.d("xyz", "result list empty");
                           throw Exceptions.propagate(new Throwable("find nothing"));}
                         return files.get(0);
                       }
                     })*//*
                     .map(new Func1<File, BufferedReader>() {
                       @Override public BufferedReader call(@NonNull File file) {
                         try {
                           return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                         } catch (FileNotFoundException e) {
                           e.printStackTrace();
                           throw Exceptions.propagate(new Throwable("get reader wrong"));
                         }}})
                     .flatMap(new Func1<BufferedReader, Observable<String>>() {
                       @Override public Observable<String> call(@NonNull BufferedReader bufferedReader) {
                         List<String> lineList = new ArrayList<>();
                         try {
                           String line = bufferedReader.readLine();
                           while (line != null) {
                             lineList.add(line);
                             line = bufferedReader.readLine();
                           }
                         } catch (Exception e) {
                           e.printStackTrace();
                           throw Exceptions.propagate(new Throwable("readLine wrong"));
                         }
                         return Observable.from(lineList);
                       }})
                     .flatMap(new Func1<String, Observable<LrcLineInfo>>() {
                       @Override public Observable<LrcLineInfo> call(@NonNull String s) {
                         if (s.charAt(0) != '[' || s.charAt(1) > '9' || s.charAt(1) < '0')
                           return Observable.empty();
                         //计算此歌词出现次数
                         int num = (s.lastIndexOf("]") + 1) / 10;
                         //获取歌词内容
                         String content = s.substring(10 * num, s.length());
                         LrcLineInfo[] array = new LrcLineInfo[num];
                         for (int i = 0; i < num; ++i) {
                           LrcLineInfo lrcLineInfo = new LrcLineInfo();
                           lrcLineInfo.content = content;
                           long minute = Long.parseLong(s.substring(i * 10 + 1, i * 10 + 3));
                           double second = Double.parseDouble(s.substring(4 + i * 10, 9 + i * 10));
                           lrcLineInfo.time = minute * 60 * 1000 + (long) (second * 1000);
                           array[i] = lrcLineInfo;
                         }
                         return Observable.from(array);}})
                      .toSortedList(new Func2<LrcLineInfo, LrcLineInfo, Integer>() {
                        @Override public Integer call(LrcLineInfo lrcLineInfo, LrcLineInfo lrcLineInfo2) {
                          return lrcLineInfo.time > lrcLineInfo2.time? 1: -1;}});*/
      return Observable.create(new Observable.OnSubscribe<List<LrcLineInfo>>() {
        @Override public void call(Subscriber<? super List<LrcLineInfo>> subscriber) {
          if (!dir.exists() || !dir.isDirectory()) {
            throw Exceptions.propagate(new Throwable("dir invalid"));
          }
          File[] fileList = dir.listFiles();
          if (fileList == null || fileList.length == 0) {
            throw Exceptions.propagate(new Throwable("list empty"));
          }
          File specficFile = null;
          for (File file : fileList) {
            Log.d("xyz", "file: "+file.getName());
            if (file.getName().equals(fullSongName)) {
              specficFile = file;
              break;
            }
          }
          if (specficFile == null) {
            throw Exceptions.propagate(new Throwable("find no file"));
          }
          List<LrcLineInfo> lrcList = new ArrayList<LrcLineInfo>();
          try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(specficFile)));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
              if (line.charAt(0) != '[' || line.charAt(1) > '9' || line.charAt(1) < '0')
                continue;
              //计算此歌词出现次数
              int num = (line.lastIndexOf("]") + 1) / 10;
              //获取歌词内容
              String content = line.substring(10 * num, line.length());
              for (int i = 0; i < num; ++i) {
                LrcLineInfo lrcLineInfo = new LrcLineInfo();
                //lrcLineInfo.content = content;
                long minute = Long.parseLong(line.substring(i * 10 + 1, i * 10 + 3));
                //double second = Double.parseDouble(line.substring(4 + i * 10, 9 + i * 10));
                //lrcLineInfo.time = minute * 60 * 1000 + (long) (second * 1000);
                lrcList.add(lrcLineInfo);
              }
            }
          } catch (Exception e) {
            throw Exceptions.propagate(e);
          }
          if (lrcList.isEmpty()) throw Exceptions.propagate(new Throwable("no lrc in file"));
          Collections.sort(lrcList, new LrcComparator());
          subscriber.onNext(lrcList);
        }
      });
  }

  public static List<LrcLineInfo> toLrcList(InputStream inputStream) {
    List<LrcLineInfo> lrcList = new ArrayList<LrcLineInfo>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        if (TextUtils.isEmpty(line) || line.charAt(0) != '['
            || line.charAt(1) > '9' || line.charAt(1) < '0')
          continue;
        //计算此歌词出现次数
        int num = (line.lastIndexOf("]") + 1) / 10;
        //获取歌词内容
        String content = line.substring(10 * num, line.length());
        for (int i = 0; i < num; ++i) {
          LrcLineInfo lrcLineInfo = new LrcLineInfo();
          lrcLineInfo.setContent(content);
          long minute = Long.parseLong(line.substring(i * 10 + 1, i * 10 + 3));
          double second = Double.parseDouble(line.substring(4 + i * 10, 9 + i * 10));
          lrcLineInfo.setTime(minute * 60 * 1000 + (long) (second * 1000));
          lrcList.add(lrcLineInfo);
        }
      }
      if (lrcList.isEmpty()) return null;
      Collections.sort(lrcList, sLrcComparator);
      return lrcList;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 鉴别这个歌词是否格式正确
   * @param inputStream
   * @param maxLines 最大检查行数
   * @return true表示合法  false表示不合法
   */
  public static boolean verifyLrc(InputStream inputStream, int maxLines) {
    try {
      int hasReadLines = 0;
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        if (TextUtils.isEmpty(line)) continue;
        ++hasReadLines;
        if (hasReadLines > maxLines) return false;
        //if illegal
        if (line.charAt(0) != '[' || line.charAt(1) > '9' || line.charAt(1) < '0')
          continue;

        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  //分析line的内容,如果是歌词则把信息添加到集合lrcLineInfos
  //如果此歌词出现多次,开头会有多个时间
  private static void analyse(String line, List<LrcLineInfo> lrcLineInfos) {
    if (line.charAt(0) == '[' && line.charAt(1) <= '9' && line.charAt(1) >= '0') {
      //计算此歌词出现次数
      int num = (line.lastIndexOf("]") + 1) / 10;
      //获取歌词内容
      String content = line.substring(10 * num, line.length());

      //根据多少个时间,添加多少个lineInfo
      for (int i = 0; i < num; ++i) {
        LrcLineInfo lrcLineInfo = new LrcLineInfo();
        //long minute = Long.parseLong(line.substring(i * 10 + 1, i * 10 + 3));
        double second = Double.parseDouble(line.substring(4 + i * 10, 9 + i * 10));
        //lrcLineInfo.time = minute * 60 * 1000 + (long) (second * 1000);
        lrcLineInfos.add(lrcLineInfo);
      }
    }
  }

  //根据传入的时间和集合和上一个,计算出时间对应的歌词
  public static int getIndexByTime(List<LrcLineInfo> list, long time) {
    if (list == null || list.isEmpty()) return -1;
    int i = 0;
    for (int length = list.size(); i+1 < length; ++i) {
      LrcLineInfo lrc1 = list.get(i);
      LrcLineInfo lrc2 = list.get(i+1);
      if (lrc1.getTime() < time && time < lrc2.getTime()) break;
    }
    return i;
  }
}
