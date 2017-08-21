package com.example.hahaha.musicplayer.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamTools {
  public static boolean inputToOutput(InputStream inputStream, OutputStream outputStream) {
    BufferedInputStream bufferIn = null;
    BufferedOutputStream bufferOut = null;
    try {
      bufferIn = new BufferedInputStream(inputStream);
      bufferOut = new BufferedOutputStream(outputStream);
      int b = bufferIn.read();
      while (b != -1) {
        bufferOut.write(b);
        b = bufferIn.read();
      }
      bufferOut.flush();
      return true;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public static InputStream toCache(InputStream inputStream) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len;
    try {
      while ((len = inputStream.read(buffer)) > -1) {
        outputStream.write(buffer, 0, len);
      }
      outputStream.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeQuiet(inputStream);
    }
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  public static void closeQuiet(InputStream inputStream) {
    if (inputStream == null) return;
    try {
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
