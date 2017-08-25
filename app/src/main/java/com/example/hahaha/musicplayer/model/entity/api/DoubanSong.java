package com.example.hahaha.musicplayer.model.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoubanSong {
  Rate rating;
  @JsonProperty("author") ArrayList<Author> authorList;
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
    if (authorList == null || authorList.isEmpty()) return new Author("");
    return authorList.get(0);
  }

  public List<Author> getAuthorList() {
    return authorList;
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
