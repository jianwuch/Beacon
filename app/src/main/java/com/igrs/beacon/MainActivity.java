package com.igrs.beacon;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import base.BaseActivity;
import butterknife.BindView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bottomBar) BottomBar bottomBar;
    @BindView(R.id.main_container) FrameLayout containerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_favorites:
                        break;
                    case R.id.tab_friends:
                        break;
                    case R.id.tab_nearby:
                        break;
                    default:
                }
            }
        });
    }
}
