package com.igrs.beacon.ui.contract;

import com.igrs.beacon.model.data.FilterConfig;
import com.igrs.beacon.model.data.iBeacon;
import com.igrs.beacon.ui.basemvp.BasePresenter;
import com.igrs.beacon.ui.basemvp.IListViewView;

import java.util.List;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class MainPageContract {
    public interface IHomeView extends IListViewView<List<iBeacon>> {
        public void newBeacon();
    }

    public static abstract class IHomePresenter  extends BasePresenter<IHomeView>{

        //设置过滤条件
        public abstract void setFilterConfig(FilterConfig config);

        //设置排序类型
        public abstract void setSort(int type);

        //全选
        public abstract void chooseAll();

        //批量操作类型
        public abstract void beginBatch(int type);

        public abstract void scanBeacon();
    }
}
