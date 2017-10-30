package com.igrs.beacon;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.igrs.beacon.base.BaseActivity;
import butterknife.BindView;
import com.igrs.beacon.ui.ConnectFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;

public class MainActivity extends BaseActivity implements ConnectFragment.OnFragmentInteractionListener{
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

        showFragment();
    }

    private void showFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_container, ConnectFragment.newInstance("", "")).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
