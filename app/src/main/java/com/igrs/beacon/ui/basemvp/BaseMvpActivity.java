package com.igrs.beacon.ui.basemvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.util.DialogManager;

/**
 * Created by jove.chen on 2017/11/16.
 */

public abstract class BaseMvpActivity<Data, T extends BasePresenter> extends BaseActivity
        implements IBaseView<Data> {
    public T presenter;
    public DialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        dialogManager = new DialogManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        presenter.dettach();
        super.onDestroy();
    }

    public abstract T initPresenter();

    @Override
    public void showLoading(boolean isShow) {
       if (isShow) {
           dialogManager.showProgress();
       } else {
           dialogManager.dissmissDialog();
       }
    }

}

