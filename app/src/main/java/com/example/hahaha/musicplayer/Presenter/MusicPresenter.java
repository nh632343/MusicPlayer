package com.example.hahaha.musicplayer.Presenter;

import android.content.Context;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;


import com.example.hahaha.musicplayer.model.entity.LrcInfo;
import com.example.hahaha.musicplayer.model.entity.PlayInfo;
import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.app.MusicApp;
import com.example.hahaha.musicplayer.tools.LrcTools;
import com.example.hahaha.musicplayer.View.LrcInterface;
import com.example.hahaha.musicplayer.View.ViewInterface;

import java.lang.ref.WeakReference;

import java.util.List;
import java.util.Vector;

public class MusicPresenter implements MusicPresenterInterface {


    private static class AudioFocusState{
        boolean gainFocus=false;
        boolean shouldStart=false;

        int currentVolume;
    }

    private class MyHandler extends Handler{
        static final int GET_LRC=1;
        static final int NO_LRC=2;
        static final int NOTIFY_PROGRESS=3;

        @Override
        public void handleMessage(Message msg) {
            if (lrcInterfaceWeakReference==null)
                return;

            LrcInterface theLrcInterface=lrcInterfaceWeakReference.get();
            if (theLrcInterface==null){
                stopLoadLrcThread();
                stopNotifyProgressThread();
                lrcInterfaceWeakReference=null;
                return;
            }

            switch (msg.what){
                case GET_LRC:
                    if (myLrcInfo==null)
                        break;
                    //检查获取的LrcInfo与当前歌名是否相符
                    if (!myLrcInfo.name.equals(playInfo.currentList.get(playInfo.current).name)){
                        break;
                    }

                    theLrcInterface.onLrcAvailable(myLrcInfo);
                    break;

                case NO_LRC:
                    String songName= (String) msg.obj;
                    theLrcInterface.NoLrc(songName);
                    break;

                case NOTIFY_PROGRESS:
                    theLrcInterface.onProgressChange(mediaPlayer.getCurrentPosition());
                    break;
            }
        }
    }

    private class LoadLrcThread extends Thread{
        String songName;
        Thread runThread;
        LrcTools.BooleanHolder stopFlag=new LrcTools.BooleanHolder(true);

        //constructor
        public LoadLrcThread(String songName) {
            this.songName = songName;
        }

        @Override
        public void run() {
           /* runThread=Thread.currentThread();
            stopFlag.value=false;
            File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            InputStream inputStream= LrcTools.getInputStream(dir
                                                        ,songName+".lrc",stopFlag);
            if (stopFlag.value)
                return;
            //找不到歌词
            if (inputStream==null){
                Message message=new Message();
                message.what=MyHandler.NO_LRC;
                message.obj=songName;
                myHandler.sendMessage(message);
                return;
            }
            LrcInfo lrcInfo= LrcTools.getLrcInfo(songName,inputStream,"utf-8",stopFlag);
            if (stopFlag.value)
                return;

            myLrcInfo=lrcInfo;
            Message message=new Message();
            message.what=MyHandler.GET_LRC;
            myHandler.sendMessage(message);*/
        }

        public void stopRequest(){
            stopFlag.value=true;
            if (runThread!=null)
                runThread.interrupt();
        }
    }

    private class NotifyProgressThread extends Thread{
        int sleepTime=200;  //ms
        Thread runThread;
        LrcTools.BooleanHolder stopFlag=new LrcTools.BooleanHolder(true);

        public void stopRequest(){
            stopFlag.value=true;
            if (runThread!=null)
                runThread.interrupt();
        }

        @Override
        public void run() {
            while(stopFlag.value){
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=MyHandler.NOTIFY_PROGRESS;
                myHandler.sendMessage(message);
            }
        }
    }

    private void stopLoadLrcThread(){
        if (loadLrcThread!=null){
            loadLrcThread.stopRequest();
            loadLrcThread=null;
        }
    }

    private void stopNotifyProgressThread(){
        if (notifyProgressThread!=null){
            notifyProgressThread.stopRequest();
            notifyProgressThread=null;
        }
    }

    //member
    private boolean isRunning=false;
    private List<Song> songList;
    private LrcInfo myLrcInfo;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioFocusState audioFocusState;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer.OnCompletionListener completionListener;
    private MediaPlayer.OnPreparedListener preparedListener;
    private PlayInfo playInfo;

    private Vector<WeakReference<ViewInterface>> viewInterface_s=new Vector<>();
    private WeakReference<LrcInterface> lrcInterfaceWeakReference;

    private MyHandler myHandler=new MyHandler();
    private LoadLrcThread loadLrcThread;
    private NotifyProgressThread notifyProgressThread;

    //constructor
    public MusicPresenter() {
         //songList= ScanTools.scanAllMusic();

        mediaPlayer=new MediaPlayer();
        playInfo=new PlayInfo();
        playInfo.currentList= songList;

        audioFocusState=new AudioFocusState();
        audioManager= (AudioManager) MusicApp.appContext().getSystemService(Context.AUDIO_SERVICE);

        audioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (mediaPlayer.isPlaying()){
                    switch (focusChange){
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:  //暂时失去
                            mediaPlayer.pause();
                            audioFocusState.shouldStart=true;

                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:    //永久失去
                            mediaPlayer.pause();
                            audioFocusState.shouldStart=false;
                            audioFocusState.gainFocus=false;
                            audioManager.abandonAudioFocus(audioFocusChangeListener);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:  //应降低音量
                            audioFocusState.currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            int volume=0;
                            if (audioFocusState.currentVolume>1){
                                volume=audioFocusState.currentVolume-2;
                            }
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                                         volume,
                                                         AudioManager.FLAG_VIBRATE);
                            audioFocusState.shouldStart=false;

                            break;
                    }
                }
                //mediaplayer is not playing
                else{
                    switch (focusChange){
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                            if (audioFocusState.shouldStart)
                                 {mediaPlayer.start();
                                 audioFocusState.shouldStart=false;}
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                                         audioFocusState.currentVolume,
                                                         AudioManager.FLAG_VIBRATE);
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            audioFocusState.gainFocus=true;
                            break;
                    }
                }
            }
        };

        completionListener=new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play(nextSongNo(playInfo.current, songList), songList);

            }
        };
        mediaPlayer.setOnCompletionListener(completionListener);

        //prepare完成后通知ViewListener并开始播放
        preparedListener=new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
               notifyViewInterfaceSongChange();
                mp.start();
                if (lrcInterfaceWeakReference!=null)
                {tryLoadLrc();}
            }
        };

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                play(nextSongNo(playInfo.current, songList), songList);

                return true;
            }
        });

    }

    private void notifyViewInterfaceUriWrong(String songName){
        for (WeakReference<ViewInterface> temp: viewInterface_s){
            ViewInterface theInterface=temp.get();
            if(theInterface!=null){
                theInterface.onUriWrong(songName);
            }
        }
    }

    private void notifyViewInterfaceSongChange(){
        for (WeakReference<ViewInterface> temp: viewInterface_s){
            ViewInterface theInterface=temp.get();
            if(theInterface!=null){
                theInterface.onSongChange();
            }
        }
    }

    //return the song No that will be played
    private int nextSongNo(int current,List songList){
        if(current+1<songList.size()){
            return current+1;
        }
        else {
            return 0;
        }
    }

    //when you get the presenter,should first call method setViewListener
    //and then call this method init
    private void init(ViewInterface viewInterface){
        if(!isRunning){
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnPreparedListener(preparedListener);
                }
            });

            play(playInfo.current,playInfo.currentList);
            viewInterface.onSongChange();

            isRunning=true;
        }
        else{
            viewInterface.onSongChange();
        }
    }

    @Override
    public void addViewListener(ViewInterface viewInterface){
        init(viewInterface);
        for (WeakReference<ViewInterface> temp :viewInterface_s){
            ViewInterface theInterface=temp.get();
            if(theInterface!=null && viewInterface==theInterface)
                return;
        }
        viewInterface_s.add(new WeakReference<ViewInterface>(viewInterface));
    }

    @Override
    public void removeViewListener(ViewInterface viewInterface) {
        int i=0;
        for (;i<viewInterface_s.size();++i){
            ViewInterface theInterface=viewInterface_s.get(i).get();
            if(theInterface!=null && viewInterface==theInterface){
                break;
            }
        }
        if (i<viewInterface_s.size()){
            viewInterface_s.remove(i);
        }
    }

    private void tryLoadLrc(){

        LrcInterface lrcInterface=lrcInterfaceWeakReference.get();
        if (lrcInterface==null){
            return;
        }

        String currentName=playInfo.currentList.get(playInfo.current).name;
        if (myLrcInfo!=null && myLrcInfo.name.equals(currentName)){
            lrcInterface.onLrcAvailable(myLrcInfo);
            return;
        }

        //线程不存在,直接开线程
        if (loadLrcThread==null){
            loadLrcThread=new LoadLrcThread(currentName);
            loadLrcThread.start();
            return;
        }

        //如果loadLrcThread正在运行
        if (loadLrcThread.isAlive()){
            //名字不符合,结束当前线程,重开线程
            if (!loadLrcThread.songName.equals(currentName)){
                loadLrcThread.stopRequest();
                loadLrcThread=new LoadLrcThread(currentName);
                loadLrcThread.start();

            }
        }
        else{//没有运行
            loadLrcThread=new LoadLrcThread(currentName);
            loadLrcThread.start();
        }
    }

    private void tryStartNotifyThread(){
        if (notifyProgressThread==null || !notifyProgressThread.isAlive()){
              notifyProgressThread=new NotifyProgressThread();
            notifyProgressThread.start();
        }
    }

    @Override
    public void setLrcListener(LrcInterface lrcInterface) {
        lrcInterfaceWeakReference=new WeakReference<LrcInterface>(lrcInterface);

        tryStartNotifyThread();
        tryLoadLrc();

    }

    @Override
    public void removeLrcListener(LrcInterface lrcInterface) {
        LrcInterface theInterface=lrcInterfaceWeakReference.get();
        if (theInterface==null){
            lrcInterfaceWeakReference=null;
        }
        else{
            if (theInterface==lrcInterface){
                lrcInterfaceWeakReference=null;
            }
        }

        if (lrcInterfaceWeakReference==null){
            loadLrcThread.stopRequest();
            notifyProgressThread.stopRequest();
        }

    }

    @Override
    public String getCurrentSongName() {
        return playInfo.currentList.get(playInfo.current).name;
    }

    @Override
    public boolean isLoop() {
        return playInfo.loop;
    }

    @Override
    public boolean isStart() {
        return playInfo.isStart;
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public List<Song> getCurrentList() {
        return playInfo.currentList;
    }

    //play the specific song but when method setDataSource failed, mediaPlayer will play the next song
    //return false for request focus failed
    //return true for request focus success
    public boolean play(int i,List<Song> songList) {
        if(!audioFocusState.gainFocus){
            //didn't have focus
            //try to request focus
            int request=audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC
                                                        ,AudioManager.AUDIOFOCUS_GAIN);
            if (request != AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                //request focus failed
                return false;
            }
            //request focus success
            audioFocusState.gainFocus=true;
        }
        //have focus

        mediaPlayer.reset();

        //play the next song until success
        boolean isFail=true;
        while (isFail){
            try{
                mediaPlayer.setDataSource(MusicApp.appContext(),songList.get(i).uri);
                isFail=false;
            }catch (Exception e){
                notifyViewInterfaceUriWrong(songList.get(i).name);
                i=nextSongNo(i,songList);
            }
        }
        playInfo.current=i;
        playInfo.isStart = isRunning;

        mediaPlayer.prepareAsync();

        return true;
    }

    public void pause(){
        mediaPlayer.pause();
        playInfo.isStart=false;
    }

    public void start(){
        mediaPlayer.start();
        playInfo.isStart=true;
    }

    public void setLoop(boolean loop){
        playInfo.loop=loop;
        mediaPlayer.setLooping(loop);
    }

    @Override
    public void setProgress(int progress) {
        mediaPlayer.seekTo(progress);
    }

    @Override
    public void finish() {
        audioManager.abandonAudioFocus(audioFocusChangeListener);
        mediaPlayer.release();
    }
}
