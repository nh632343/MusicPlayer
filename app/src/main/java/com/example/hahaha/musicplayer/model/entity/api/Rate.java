package com.example.hahaha.musicplayer.model.entity.api;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate {
  String average;
  int numRaters;

  public String getAverage() {
    return average;
  }

  public int getNumRaters() {
    return numRaters;
  }
}
