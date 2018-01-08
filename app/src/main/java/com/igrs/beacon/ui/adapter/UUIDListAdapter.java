package com.igrs.beacon.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.igrs.beacon.R;
import com.igrs.beacon.model.data.UUIDBean;

import java.util.List;

/**
 * Created by jianw on 17-12-3.
 */

public class UUIDListAdapter extends BaseQuickAdapter<UUIDBean, BaseViewHolder> {
    public UUIDListAdapter(int layoutResId, List<UUIDBean> mDatas) {
        super(layoutResId, mDatas);
    }

    public UUIDListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UUIDBean item) {
        helper.setText(R.id.name, item.name).setText(R.id.uuid, item.uuid);
    }
}
