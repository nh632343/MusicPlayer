package com.example.hahaha.musicplayer.feature.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_exit:
        stopService(ServiceMessageHelper.createIntent());
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
