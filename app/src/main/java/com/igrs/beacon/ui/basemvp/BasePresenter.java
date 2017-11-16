package com.igrs.beacon.ui.basemvp;

/**
 * Created by jove.chen on 2017/11/16.
 */

public abstract class BasePresenter<T> {
    public T mView;

    public void attach(T view) {
        this.mView = view;
    }

    public void dettach(){
        mView = null;
    }

    public abstract void start();
}
