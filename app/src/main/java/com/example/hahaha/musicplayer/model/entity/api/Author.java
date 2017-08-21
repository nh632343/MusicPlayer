package com.example.hahaha.musicplayer.model.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
  String name;

  public String getName() {
    return name;
  }
}
