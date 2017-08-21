package com.example.hahaha.musicplayer.widget;

import java.util.ArrayList;

public class ComObservable<T> {
  private final ArrayList<ComSubscriber<T>> mObservers = new ArrayList<>();

  public void addSubscriber(ComSubscriber<T> subscriber) {
    if (subscriber == null) return;
    if (mObservers.contains(subscriber)) return;
    mObservers.add(subscriber);
  }

  public void removeSubscriber(ComSubscriber<T> subscriber) {
    if (subscriber == null) return;
    mObservers.remove(subscriber);
  }

  public void broadcast(T t) {
    for(ComSubscriber<T> subscriber : mObservers) {
      subscriber.onChange(t);
    }
  }
}
