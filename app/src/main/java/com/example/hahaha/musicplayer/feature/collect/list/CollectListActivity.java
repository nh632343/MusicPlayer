package com.example.hahaha.musicplayer.feature.collect.list;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.app.Navigator;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;
import com.example.hahaha.musicplayer.feature.base.header.FragmentStack;
import java.util.List;

public class CollectListActivity extends BaseActivity
implements FragmentStack {

  public static void startView(Context context) {
    Intent intent = new Intent(context, CollectListActivity.class);
    intent.putExtra(Navigator.EXTRA_COLLECT_LIST_OPTION, Navigator.COLLECT_LIST_OPTION_VIEW);
    context.startActivity(intent);
  }

  public static void startAdd(Context context, @NonNull List<Long> songIds) {
    if (songIds == null || songIds.isEmpty())
      throw new NullPointerException("songIds can not be empty");
    long[] extra = new long[songIds.size()];
    for (int i = 0; i < extra.length; ++i) {
      extra[i] = songIds.get(i);
    }
    startAdd(context, extra);
  }

  public static void startAdd(Context context, @NonNull long[] songIds) {
    if (songIds == null || songIds.length == 0)
      throw new NullPointerException("songIds can not be empty");
    Intent intent = new Intent(context, CollectListActivity.class);
    intent.putExtra(Navigator.EXTRA_COLLECT_LIST_OPTION, Navigator.COLLECT_LIST_OPTION_ADD);
    intent.putExtra(Navigator.EXTRA_COLLECT_LIST_SONG_ID_ARRAY, songIds);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.single_frame);
    int option = getIntent().getIntExtra(Navigator.EXTRA_COLLECT_LIST_OPTION, Navigator.COLLECT_LIST_OPTION_VIEW);
    Fragment fragment = null;
    if (option == Navigator.COLLECT_LIST_OPTION_VIEW) fragment = CollectListFragment.newViewInstance();
    if (option == Navigator.COLLECT_LIST_OPTION_ADD)
      fragment = CollectListFragment.newAddInstance(getIntent().getLongArrayExtra(Navigator.EXTRA_COLLECT_LIST_SONG_ID_ARRAY));

    if (fragment == null) throw new NullPointerException("option: "+String.valueOf(option)+"is invalid");

    getSupportFragmentManager().beginTransaction()
        .replace(android.R.id.content, fragment)
        .commit();
  }

  @Override public void startFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(android.R.id.content, fragment)
        .addToBackStack(null)
        .commit();
  }
}
