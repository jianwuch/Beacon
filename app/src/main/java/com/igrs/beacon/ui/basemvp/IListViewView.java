package com.igrs.beacon.ui.basemvp;

/**
 * Created by jove.chen on 2017/11/16.
 */

public interface IListViewView<T> extends IBaseView<T> {
    //加载更多
    void loadMorePage();

    //刷新
    void refresh();

    //notify list
    void notifyDataSet();
}
