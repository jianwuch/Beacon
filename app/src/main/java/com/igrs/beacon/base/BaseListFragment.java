package com.igrs.beacon.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.igrs.beacon.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/10.
 */

public abstract class BaseListFragment<T> extends BaseFragment {
    @BindView(R.id.recycle_view) RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;
    private View mRootView;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private RecyclerView.Adapter adapter;
    protected List<T> mDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.frg_list_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置列表属性
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(
                new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        adapter = initAdapter();
        if (adapter != null) {
            recycleView.setAdapter(initAdapter());
        } else {
            throw new NullPointerException(this.getClass().getName()+"has not set adapter");
        }
        //刷新
        onRefreshListener = initRefreshListener();
        if (null != onRefreshListener) {
            swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        }
        mDatas = new ArrayList<>();
        //加载数据
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void notifyAdapter() {
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }
    protected abstract RecyclerView.Adapter initAdapter();
    protected abstract SwipeRefreshLayout.OnRefreshListener initRefreshListener();
    protected abstract void loadData();
}
