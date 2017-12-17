package com.igrs.beacon.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.igrs.beacon.util.DialogManager;

/**
 * Created by jove.chen on 2017/10/27.
 */

public class BaseActivity extends AppCompatActivity {
    public static final String INTENT_KEY = "key1";
    private Unbinder mUnbinder;
    public DialogManager dialogManager;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(BaseActivity.this);
        dialogManager = new DialogManager(this);
    }

    /**
     * 初始化 Toolbar
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showLoading(false);
    }

    public void showLoading(boolean isShow) {
        if (null == dialogManager) {
            return;
        }
        if (isShow) {
            dialogManager.showProgress();
        } else {
            dialogManager.dissmissDialog();
        }
    }
}
