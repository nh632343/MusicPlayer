package com.example.hahaha.musicplayer.model.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchLrc {
  int count;
  @JsonProperty("result") ArrayList<LrcAddress> lrcAddressList;

  public int getCount() {
    return count;
  }

  public ArrayList<LrcAddress> getLrcAddressList() {
    return lrcAddressList;
  }
}
