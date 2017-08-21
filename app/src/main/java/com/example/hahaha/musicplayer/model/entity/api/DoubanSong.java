package com.example.hahaha.musicplayer.model.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoubanSong {
  Rate rating;
  Author author;
  String title;
  @JsonProperty("alt_title") String albumTitle;
  String image;
  String id;

  public DoubanSong() {
  }

  public Rate getRating() {
    return rating;
  }

  public Author getAuthor() {
    return author;
  }

  public String getImageUrl() {
    return image;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAlbumTitle() {
    return albumTitle;
  }
}
