package com.igrs.beacon.ui.basemvp;

/**
 * Created by jove.chen on 2017/11/16.
 */

public interface IBaseView<T> {
    //显示耗时加载进度
    void showLoading(boolean isShow);

    //显示数据
    void showDataFromPresenter(T data);
}
