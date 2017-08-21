package com.example.hahaha.musicplayer.model.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LrcAddress {
  String lrc;

  public String getAddress() {
    return lrc;
  }
}
