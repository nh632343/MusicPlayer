/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/hahaha/workspace/android_project/MusicPlayer/app/src/main/aidl/com/example/hahaha/musicplayer/PositionListener.aidl
 */
package com.example.hahaha.musicplayer.service.aid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.example.hahaha.musicplayer.model.entity.internal.PositionInfo;
import java.lang.ref.WeakReference;

public interface PositionListener extends android.os.IInterface {
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements PositionListener {

    /*----------------MainThread Handler---------------------------*/
    private static class MyHandler extends Handler {
      WeakReference<PositionListener> mWeakRef;
      MyHandler(PositionListener listener) {
        super(Looper.getMainLooper());
        mWeakRef = new WeakReference<>(listener);
      }

      @Override public void handleMessage(Message msg) {
        PositionListener listener = mWeakRef.get();
        if (listener == null) return;
        try {
          switch (msg.what) {
            case TRANSACTION_onPositionChange:
              listener.onPositionChange((PositionInfo) msg.obj);
              return;
          }
        } catch (Exception e) {e.printStackTrace();}
        super.handleMessage(msg);
      }
    }
    /*----------------MainThread Handler End-------------------------*/

    private static final java.lang.String DESCRIPTOR =
        "com.example.hahaha.musicplayer.PositionListener";
    private MyHandler myHandler;

    /** Construct the stub at attach it to the interface. */
    public Stub() {
      this.attachInterface(this, DESCRIPTOR);
      myHandler = new MyHandler(this);
    }

    /**
     * Cast an IBinder object into an com.example.hahaha.musicplayer.PositionListener interface,
     * generating a proxy if needed.
     */
    public static PositionListener asInterface(
        android.os.IBinder obj) {
      if ((obj == null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin != null) && (iin instanceof PositionListener))) {
        return ((PositionListener) iin);
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
        case TRANSACTION_onPositionChange: {
          data.enforceInterface(DESCRIPTOR);
          PositionInfo positionInfo = data.readParcelable(getClass().getClassLoader());
          Message msg = Message.obtain();
          msg.what = TRANSACTION_onPositionChange;
          msg.obj = positionInfo;
          myHandler.sendMessage(msg);
          reply.writeNoException();
          return true;
        }
      }
      return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements PositionListener {
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

      @Override public void onPositionChange(PositionInfo positionInfo)
          throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeParcelable(positionInfo, 0);
          mRemote.transact(Stub.TRANSACTION_onPositionChange, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }

    static final int TRANSACTION_onPositionChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }

  public void onPositionChange(PositionInfo positionInfo) throws android.os.RemoteException;
}
