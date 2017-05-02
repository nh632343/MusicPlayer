/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/hahaha/workspace/android_project/MusicPlayer/app/src/main/aidl/com/example/hahaha/musicplayer/SongChangeListener.aidl
 */
package com.example.hahaha.musicplayer.feature.service.aid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

public interface SongChangeListener extends android.os.IInterface {

  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements SongChangeListener {

    /*----------------MainThread Handler---------------------------*/
    private static class MyHandler extends Handler {
      WeakReference<SongChangeListener> mWeakRef;
      MyHandler(SongChangeListener listener) {
        super(Looper.getMainLooper());
        mWeakRef = new WeakReference<>(listener);
      }

      @Override public void handleMessage(Message msg) {
        SongChangeListener listener = mWeakRef.get();
        if (listener == null) return;
        try {
          switch (msg.what) {
            case TRANSACTION_onPreparing:
              listener.onPreparing();   return;
            case TRANSACTION_onSongChange:
              listener.onSongChange((String)msg.obj, msg.arg1 != 0, msg.arg2);
              return;
          }
        } catch (Exception e) {e.printStackTrace();}
        super.handleMessage(msg);
      }
    }
    /*----------------MainThread Handler End-------------------------*/

    private static final java.lang.String DESCRIPTOR =
        "com.example.hahaha.musicplayer.SongChangeListener";
    private MyHandler myHandler;

    /** Construct the stub at attach it to the interface. */
    public Stub() {
      this.attachInterface(this, DESCRIPTOR);
      myHandler = new MyHandler(this);
    }

    /**
     * Cast an IBinder object into an com.example.hahaha.musicplayer.SongChangeListener interface,
     * generating a proxy if needed.
     */
    public static SongChangeListener asInterface(
        android.os.IBinder obj) {
      if ((obj == null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin != null) && (iin instanceof SongChangeListener))) {
        return ((SongChangeListener) iin);
      }
      return new Proxy(obj);
    }

    @Override public android.os.IBinder asBinder() {
      return this;
    }

    @Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
        throws android.os.RemoteException {
      switch (code) {
        case INTERFACE_TRANSACTION: {
          reply.writeString(DESCRIPTOR);
          return true;
        }
        case TRANSACTION_onPreparing: {
          data.enforceInterface(DESCRIPTOR);
          Message msg = Message.obtain();
          msg.what = TRANSACTION_onPreparing;
          myHandler.sendMessage(msg);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_onSongChange: {
          data.enforceInterface(DESCRIPTOR);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          Message msg = Message.obtain();
          msg.what = TRANSACTION_onSongChange;
          msg.obj = _arg0;
          msg.arg1 = _arg1;
          msg.arg2 = _arg2;
          myHandler.sendMessage(msg);
          reply.writeNoException();
          return true;
        }
      }
      return super.onTransact(code, data, reply, flags);
    }

    static class Proxy implements SongChangeListener {
      private android.os.IBinder mRemote;

      Proxy(android.os.IBinder remote) {
        mRemote = remote;
      }

      @Override public android.os.IBinder asBinder() {
        return mRemote;
      }

      public java.lang.String getInterfaceDescriptor() {
        return DESCRIPTOR;
      }

      @Override public void onPreparing() throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          mRemote.transact(Stub.TRANSACTION_onPreparing, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }

      @Override
      public void onSongChange(java.lang.String songName, boolean isPlaying, int playOrder)
          throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(songName);
          _data.writeInt(((isPlaying) ? (1) : (0)));
          _data.writeInt(playOrder);
          mRemote.transact(Stub.TRANSACTION_onSongChange, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }

    static final int TRANSACTION_onPreparing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onSongChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }

  public void onPreparing() throws android.os.RemoteException;

  public void onSongChange(java.lang.String songName, boolean isPlaying, int playOrder)
      throws android.os.RemoteException;
}
