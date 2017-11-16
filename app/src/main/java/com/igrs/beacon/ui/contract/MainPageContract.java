package com.igrs.beacon.ui.contract;

import com.igrs.beacon.moudle.data.BeaconWithCheckable;
import com.igrs.beacon.ui.basemvp.BasePresenter;
import com.igrs.beacon.ui.basemvp.IListViewView;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class MainPageContract {
    public interface IHomeView extends IListViewView<List<BeaconWithCheckable>> {
        public void newBeacon();
    }

    public static abstract class IHomePresenter  extends BasePresenter<IHomeView>{

        //设置过滤条件
        public abstract void setFilter();

        public abstract void scanBeacon();
    }
}
