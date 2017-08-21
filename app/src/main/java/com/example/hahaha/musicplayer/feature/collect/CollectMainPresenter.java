package com.example.hahaha.musicplayer.feature.collect;

import android.os.Bundle;
import com.example.hahaha.musicplayer.feature.base.BasePresenter;
import com.example.hahaha.musicplayer.manager.database.DatabaseManager;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;
import com.example.hahaha.musicplayer.widget.ComSubscriber;
import java.util.List;
import org.litepal.crud.DataSupport;

public class CollectMainPresenter extends BasePresenter<CollectMainFragment> {
  private ComSubscriber<Object> mListener;

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    mListener = new ComSubscriber<Object>() {
      @Override public void onChange(Object ignore) {
        CollectMainFragment fragment = getView();
        if (fragment == null) return;
        List<Collect> collectList = DataSupport.findAll(Collect.class);
        fragment.setCollectList(collectList);
        fragment.setCollectNum(collectList.size());
      }
    };
  }

  @Override protected void onTakeView(CollectMainFragment collectMainFragment) {
    super.onTakeView(collectMainFragment);
    DatabaseManager.getInstance().addCollectListener(mListener);
  }

  @Override protected void onDropView() {
    super.onDropView();
    DatabaseManager.getInstance().removeCollectListener(mListener);
  }

  @Override protected void onFirstTakeView(CollectMainFragment collectMainFragment) {
    List<Collect> collectList = DataSupport.findAll(Collect.class);
    collectMainFragment.setCollectList(collectList);
    collectMainFragment.setCollectNum(collectList.size());
  }
}
