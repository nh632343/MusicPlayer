/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/hahaha/workspace/android_project/MusicPlayer/app/src/main/aidl/com/example/hahaha/musicplayer/IMusicManager.aidl
 */
package com.example.hahaha.musicplayer.feature.service.aid;

import android.os.RemoteException;
import com.example.hahaha.musicplayer.model.entity.Song;

public interface IMusicManager extends android.os.IInterface {
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder
      implements IMusicManager {
    private static final java.lang.String DESCRIPTOR =
        "com.example.hahaha.musicplayer.IMusicManager";

    /** Construct the stub at attach it to the interface. */
    public Stub() {
      this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * Cast an IBinder object into an com.example.hahaha.musicplayer.IMusicManager interface,
     * generating a proxy if needed.
     */
    public static IMusicManager asInterface(android.os.IBinder obj) {
      if ((obj == null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin != null) && (iin instanceof IMusicManager))) {
        return ((IMusicManager) iin);
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
        case TRANSACTION_getSongList: {
          data.enforceInterface(DESCRIPTOR);
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<Song> _result = this.getSongList(_arg0);
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_registerSongChangeListener: {
          data.enforceInterface(DESCRIPTOR);
          SongChangeListener listener
              = SongChangeListener.Stub.asInterface(data.readStrongBinder());
          this.registerSongChangeListener(listener);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unregisterSongChangeListener: {
          data.enforceInterface(DESCRIPTOR);
          SongChangeListener listener
              = SongChangeListener.Stub.asInterface(data.readStrongBinder());
          this.unregisterSongChangeListener(listener);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_registerPositionListener: {
          data.enforceInterface(DESCRIPTOR);
          PositionListener listener
              = PositionListener.Stub.asInterface(data.readStrongBinder());
          this.registerPositionListener(listener);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unregisterPositionListener: {
          data.enforceInterface(DESCRIPTOR);
          PositionListener listener
              = PositionListener.Stub.asInterface(data.readStrongBinder());
          this.unregisterPositionListener(listener);
          reply.writeNoException();
          return true;
        }
      }
      return super.onTransact(code, data, reply, flags);
    }

    static class Proxy implements IMusicManager {
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

      @Override
      public java.util.List<Song> getSongList(int songListType)
          throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<Song> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(songListType);
          mRemote.transact(Stub.TRANSACTION_getSongList, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(Song.CREATOR);
        } finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }

      @Override
      public void registerSongChangeListener(SongChangeListener listener)
          throws RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
          mRemote.transact(Stub.TRANSACTION_registerSongChangeListener, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }

      @Override
      public void unregisterSongChangeListener(SongChangeListener listener)
          throws RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
          mRemote.transact(Stub.TRANSACTION_unregisterSongChangeListener, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }

      @Override
      public void registerPositionListener(PositionListener listener)
          throws RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
          mRemote.transact(Stub.TRANSACTION_registerPositionListener, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }

      @Override
      public void unregisterPositionListener(PositionListener listener)
          throws RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
          mRemote.transact(Stub.TRANSACTION_unregisterPositionListener, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }

    static final int TRANSACTION_getSongList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_registerSongChangeListener = FIRST_CALL_TRANSACTION + 1;
    static final int TRANSACTION_unregisterSongChangeListener = FIRST_CALL_TRANSACTION + 2;
    static final int TRANSACTION_registerPositionListener = FIRST_CALL_TRANSACTION + 3;
    static final int TRANSACTION_unregisterPositionListener = FIRST_CALL_TRANSACTION + 4;
  }

  public java.util.List<Song> getSongList(int songListType)
      throws android.os.RemoteException;

  public void registerSongChangeListener(SongChangeListener listener)
      throws android.os.RemoteException;

  public void unregisterSongChangeListener(SongChangeListener listener)
      throws android.os.RemoteException;

  public void registerPositionListener(PositionListener listener)
      throws android.os.RemoteException;

  public void unregisterPositionListener(PositionListener listener)
      throws android.os.RemoteException;
}
