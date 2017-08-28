package com.example.hahaha.musicplayer.feature.chooselrc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseActivity;
import com.example.hahaha.musicplayer.feature.play.songlrc.LrcFragment;
import com.example.hahaha.musicplayer.model.entity.internal.LrcLineInfo;
import com.example.hahaha.musicplayer.service.proxy.MusicManagerProxy;
import com.example.hahaha.musicplayer.tools.XStatusBarHelper;
import com.example.hahaha.musicplayer.widget.ArrayPagerAdapter;
import com.example.hahaha.musicplayer.widget.TitleBar;
import java.util.List;
import nucleus.factory.RequiresPresenter;
import rx.functions.Func1;
import support.ui.utilities.ViewUtils;

@RequiresPresenter(ChooseLrcPresenter.class)
public class ChooseLrcActivity extends BaseActivity<ChooseLrcPresenter> {
  public static void start(Context context) {
    Intent intent = new Intent(context, ChooseLrcActivity.class);
    context.startActivity(intent);
  }


  @BindView(R.id.titlebar) TitleBar mTitlebar;
  @BindView(R.id.edt_song_name) EditText mEdtSongName;
  @BindView(R.id.edt_artist_name) EditText mEdtArtistName;
  @BindView(R.id.view_pager_lrc) ViewPager mViewPagerLrc;
  @BindView(R.id.progress_bar) ProgressBar mProgressBar;
  @BindView(R.id.txt_find_fail) TextView mTxtFindFail;
  private ArrayPagerAdapter<List<LrcLineInfo>> mAdapter;
  private Func1<List<LrcLineInfo>, Fragment> mGetFragment;

  @OnClick(R.id.txt_search) void search() {
    getPresenter().search(mEdtSongName.getText().toString()
        ,mEdtArtistName.getText().toString(), this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_lrc);
    XStatusBarHelper.forceFitsSystemWindows(this);
    XStatusBarHelper.immersiveStatusBar(this);
    XStatusBarHelper.setHeightAndPadding(this, mTitlebar);
    mTitlebar.setBackClickListener(v -> finish());
    mGetFragment = new Func1<List<LrcLineInfo>, Fragment>() {
      @Override public Fragment call(List<LrcLineInfo> lrcLineInfos) {
        return LrcFragment.newManualInstance(lrcLineInfos);
      }
    };

    String songName = MusicManagerProxy.getInstance().getCurrentSongName();
    if (TextUtils.isEmpty(songName)) {
      ViewUtils.setInvisible(mViewPagerLrc, true);
      ViewUtils.setInvisible(mTxtFindFail, true);
      ViewUtils.setInvisible(mProgressBar, true);
      return;
    }
    mEdtSongName.setText(songName);
    search();
  }

  void showLoading() {
    ViewUtils.setInvisible(mViewPagerLrc, true);
    ViewUtils.setInvisible(mTxtFindFail, true);
    ViewUtils.setInvisible(mProgressBar, false);
  }

  void showFail() {
    ViewUtils.setInvisible(mViewPagerLrc, true);
    ViewUtils.setInvisible(mTxtFindFail, false);
    ViewUtils.setInvisible(mProgressBar, true);
  }

  void showContent(List<List<LrcLineInfo>> lrcList) {
    ViewUtils.setInvisible(mViewPagerLrc, false);
    ViewUtils.setInvisible(mTxtFindFail, true);
    ViewUtils.setInvisible(mProgressBar, true);
    if (mAdapter == null) {
      mAdapter = new ArrayPagerAdapter<>(getSupportFragmentManager(),
          lrcList, mGetFragment);
      mViewPagerLrc.setAdapter(mAdapter);
    } else {
      mAdapter.changeData(lrcList);
    }
  }
}
