package com.example.hahaha.musicplayer.tools;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.model.entity.Song;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class ScanTools {

  //scan all music on the phone
  public static ArrayList<Song> scanAllMusic() {
    ArrayList<Song> songList = new ArrayList<>();
    ContentResolver contentResolver = MusicApp.appContext().getContentResolver();
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Cursor cursor = contentResolver.query(uri, null, null, null, null);
    //find nothing
    if (cursor == null || ! cursor.moveToFirst()) {
      if (cursor != null) {
        cursor.close();
      }
      return null;
    }
    //find something
    int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
    int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
    do {
      Song song = new Song();
      song.name = cursor.getString(titleColumn);
      song.id = cursor.getLong(idColumn);
      song.uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id);
      songList.add(song);
    } while (cursor.moveToNext());
    cursor.close();
    return songList;
  }

  /*public static Observable<ArrayList<Song>> scanAllMusic() {
    return Observable.just(MusicApp.appContext().getContentResolver())
                     .map(new Func1<ContentResolver, Cursor>() {
                       @Override public Cursor call(ContentResolver resolver) {
                         return resolver.query(
                             MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                             null, null, null, null);}})
                     .filter(new Func1<Cursor, Boolean>() {
                       @Override public Boolean call(Cursor cursor) {
                         if (cursor == null || !cursor.moveToFirst()) {
                           if (cursor != null) cursor.close();
                           throw Exceptions.propagate(new Throwable("find nothing"));
                         }
                         return true;}})
                     .map(new Func1<Cursor, ArrayList<Song>>() {
                       @Override public ArrayList<Song> call(Cursor cursor) {
                         int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                         int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                         ArrayList<Song> list = new ArrayList<>();
                         do {
                           Song song = new Song();
                           song.name = cursor.getString(titleColumn);
                           song.id = cursor.getLong(idColumn);
                           song.uri = ContentUris.withAppendedId(
                               MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id);
                           list.add(song);
                         } while (cursor.moveToNext());
                         cursor.close();
                         return list;
                       }});
  }*/
}
