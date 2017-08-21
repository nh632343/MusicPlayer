package com.example.hahaha.musicplayer.tools;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.example.hahaha.musicplayer.model.entity.internal.Song;
import com.example.hahaha.musicplayer.app.MusicApp;
import java.util.ArrayList;
import rx.Observable;
import rx.exceptions.Exceptions;

public class ScanTools {
  private static String[] projection = {
      MediaStore.Audio.Media._ID,
      MediaStore.Audio.Media.TITLE,
      //MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.ARTIST,
      //MediaStore.Audio.Media.DURATION,
      //MediaStore.Audio.Media.SIZE
  };

  //scan all music on the phone
  /*public static ArrayList<Song> scanAllMusic() {
    ArrayList<Song> songList = new ArrayList<>();
    ContentResolver contentResolver = MusicApp.appContext().getContentResolver();
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Cursor cursor = contentResolver.query(uri, projection, null, null, null);
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
  }*/

  public static Observable<ArrayList<Song>> scanAllMusic() {
    return Observable.just(MusicApp.appContext().getContentResolver())
        .map(resolver -> {
          ArrayList<Song> songList = new ArrayList<>();
          Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
          Cursor cursor = resolver.query(uri, null, null, null, null);
          //find nothing
          if (cursor == null || ! cursor.moveToFirst()) {
            if (cursor != null) {
              cursor.close();
            }
            throw Exceptions.propagate(new Throwable());
          }

          int nameCol = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
          int albumCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
          int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
          //int durationCol = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
          //int sizeCol = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
          int artistCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
          //int urlCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

          do {
            Song song = new Song(cursor.getString(nameCol), cursor.getString(artistCol), cursor.getString(albumCol),
                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idCol))
            );
            Log.d("xyz", song.toString());
            songList.add(song);
          } while (cursor.moveToNext());
          cursor.close();
          return songList;
        });
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
