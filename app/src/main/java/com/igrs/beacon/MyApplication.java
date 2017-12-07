package com.igrs.beacon;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.igrs.beacon.config.FilterManager;
import com.inuker.bluetooth.library.BluetoothContext;

import greendao.DaoMaster;
import greendao.GreenDaoHelper;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BluetoothContext.set(this);
        GreenDaoHelper.initDatabase();
        FilterManager.init(this);
    }
}
