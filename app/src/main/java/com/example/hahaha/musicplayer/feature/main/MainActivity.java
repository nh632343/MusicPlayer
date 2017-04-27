package com.example.hahaha.musicplayer.feature.main;

import android.os.Bundle;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;

import com.example.hahaha.musicplayer.feature.service.ServiceMessageHelper;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    startService(ServiceMessageHelper.createIntent());
  }
}
