package com.example.hahaha.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.hahaha.musicplayer.Info.LrcInfo;
import com.example.hahaha.musicplayer.Info.LrcLineInfo;
import com.example.hahaha.musicplayer.Info.SongInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hahaha on 9/16/16.
 */
public class MyTools {
    //scan the music on the phone
    public static List<SongInfo> scanMusic(){
        ArrayList<SongInfo> songInfoArrayList=new ArrayList<>();
        ContentResolver contentResolver= MyApp.getContext().getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        //find nothing
        if (cursor==null || !cursor.moveToFirst()){
            if (cursor != null) {
                cursor.close();
            }
            return songInfoArrayList;
        }
        //find something
        int titleColumn=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int idColumn=cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        do {
            SongInfo songInfo=new SongInfo();
            songInfo.name=cursor.getString(titleColumn);
            long id=cursor.getLong(idColumn);
            songInfo.uri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
            songInfoArrayList.add(songInfo);
        }while(cursor.moveToNext());
        cursor.close();
        return songInfoArrayList;
    }

    public static class BooleanHolder{
        volatile public boolean value;

        public BooleanHolder(boolean value) {
            this.value = value;
        }
    }

    //通过歌词的时间比较
    private static class LrcComparator implements Comparator<LrcLineInfo>{
        @Override
        public int compare(LrcLineInfo o1, LrcLineInfo o2) {
            return o1.time>o2.time? 1:-1;
        }
    }

    //给定目录,歌词文件名(包括后缀),查找歌词
    //如果目录不合法或找不到或被终止,返回null
    public static InputStream getInputStream(File dir,String fullSongName,BooleanHolder stopFlag){
        if (dir!=null && dir.exists() && dir.isDirectory()){
             File[] files=dir.listFiles();
            for (File temp: files){
                if (stopFlag.value)
                    return null;
                if (temp.isFile() && temp.getName().equals(fullSongName)){
                    try {
                        return new FileInputStream(temp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


    //booleanHolder的值为true表示要停止
    //如果被终止,将返回null
    public static LrcInfo getLrcInfo(String songName,InputStream inputStream,String charSet,
                                      BooleanHolder stopFlag){
        LrcInfo lrcInfo=new LrcInfo();
        lrcInfo.name=songName;
        ArrayList<LrcLineInfo> lrcLineInfos=new ArrayList<>();
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,charSet));
            String line = reader.readLine();
            while(line!=null){
                if (stopFlag.value){
                     break;
                }
                analyse(line,lrcLineInfos);
                line=reader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (stopFlag.value){
            return null;
        }
        Collections.sort(lrcLineInfos,new LrcComparator());
        lrcInfo.lrcLineInfoList=lrcLineInfos;
        return lrcInfo;
    }

    private static InputStreamReader getReader(InputStream inputStream){
        byte[] bytes=new byte[3];
        try {
            inputStream.read(bytes);
            if (bytes[0]==-17&&bytes[1]==-69&&bytes[2]==-65)
                return new InputStreamReader(inputStream,"utf-8");
            else
                return new InputStreamReader(inputStream,"gbk");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //分析line的内容,如果是歌词则把信息添加到集合lrcLineInfos
    //如果此歌词出现多次,开头会有多个时间
    private static void analyse(String line, List<LrcLineInfo> lrcLineInfos){
        if (line.charAt(0)=='[' && line.charAt(1)<='9'&& line.charAt(1)>='0'){
            //计算此歌词出现次数
            int num=(line.lastIndexOf("]")+1)/10;
            //获取歌词内容
            String content=line.substring(10*num,line.length());

            //根据多少个时间,添加多少个lineInfo
            for (int i=0;i<num;++i){
                LrcLineInfo lrcLineInfo=new LrcLineInfo();
                lrcLineInfo.content=content;
                long minute=Long.parseLong(line.substring(i*10+1,i*10+3));
                double  second=Double.parseDouble(line.substring(4+i*10,9+i*10));
                lrcLineInfo.time=minute*60*1000+(long)(second*1000);
                lrcLineInfos.add(lrcLineInfo);
            }
        }
    }

    //根据传入的时间和集合和上一个,计算出时间对应的歌词
    public static int getNoFromTime(List<LrcLineInfo> list,int lastNo,long time){
        if (lastNo<0){
            lastNo=0;
        }

        if (list.get(lastNo).time==time){
            return lastNo;
        }
        else if (list.get(lastNo).time<time){ //往后寻找
            int temp=lastNo;
            while(temp+1<list.size()){
                if (time<list.get(temp+1).time)
                    break;
                ++temp;
            }
            return temp;
        }
        else{ //往前寻找
            int temp=lastNo;
            while(temp-1>=0){

                if (time>list.get(temp-1).time) {
                    --temp;
                    break;
                }
                --temp;
            }
            return temp;
        }
    }
}
