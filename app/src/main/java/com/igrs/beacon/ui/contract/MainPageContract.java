package com.igrs.beacon.ui.contract;

import com.igrs.beacon.moudle.data.BleBeacon;
import com.igrs.beacon.ui.basemvp.BasePresenter;
import com.igrs.beacon.ui.basemvp.IListViewView;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class MainPageContract {
    public interface IHomeView extends IListViewView<List<BleBeacon>> {
        void newBeacon();
    }

    public abstract class IHomePresenter  extends BasePresenter{

        //设置过滤条件
        abstract void setFilter();

        abstract void scanBeacon();
    }
}
