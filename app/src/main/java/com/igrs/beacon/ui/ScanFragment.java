package com.igrs.beacon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.igrs.beacon.base.BaseFragment;
import com.igrs.beacon.base.BaseListFragment;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanFragment extends BaseListFragment {

    @Override
    protected RecyclerView.Adapter initAdapter() {
        return null;
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener initRefreshListener() {
        return null;
    }
}
