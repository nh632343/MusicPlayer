package com.example.hahaha.musicplayer.feature.collect.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.feature.base.BaseFragment;
import com.example.hahaha.musicplayer.manager.database.DatabaseManager;
import com.example.hahaha.musicplayer.model.entity.internal.Collect;
import support.ui.utilities.ToastUtils;

public class AddCollectFragment extends BaseFragment {
  public static AddCollectFragment newInstance() {
    return new AddCollectFragment();
  }

  @BindView(R.id.edt_add) EditText mEdtAdd;
  @BindView(R.id.txt_confirm) TextView mTxtConfirm;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_add_collect;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mTxtConfirm.setOnClickListener(v -> {
      String s = mEdtAdd.getText().toString().trim();
      if (TextUtils.isEmpty(s)) {
        ToastUtils.toast("can not be empty");
        return;
      }
      if (new Collect(s).save()) {
        hideSoftInputMethod();
        backPress();
        DatabaseManager.getInstance().broadcastCollectChange();
        return;
      }
      ToastUtils.toast(s + " already exist");
    });
  }
}
