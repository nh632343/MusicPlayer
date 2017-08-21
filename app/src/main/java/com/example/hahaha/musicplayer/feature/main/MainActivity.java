package com.example.hahaha.musicplayer.feature.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;

import com.example.hahaha.musicplayer.feature.base.header.FragmentStack;
import com.example.hahaha.musicplayer.feature.collect.CollectMainFragment;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter>
implements FragmentStack {
  public static void start(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, new CollectMainFragment())
        .commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_exit:
        MusicManagerProxy.getInstance().exit();
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /*public void openAllSong() {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, new AllSongFragment())
        .addToBackStack(null)
        .commit();
  }*/

  @Override public void startFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, fragment)
        .addToBackStack(null)
        .commit();
  }
}
