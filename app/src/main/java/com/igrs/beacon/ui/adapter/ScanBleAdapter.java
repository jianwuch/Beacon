package com.igrs.beacon.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igrs.beacon.R;
import com.igrs.beacon.moudle.data.BleBeacon;

import java.util.List;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanBleAdapter extends RecyclerView.Adapter<ScanBleAdapter.BeaconVH> {
    private List<BleBeacon> mDatas;

    public void setmDatas(List<BleBeacon> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 添加逻辑包含重复的数据剔除
     */
    public void add() {

    }

    @Override
    public BeaconVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_device_info,
                parent, false);
        return new BeaconVH(view);
    }

    @Override
    public void onBindViewHolder(BeaconVH holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class BeaconVH extends RecyclerView.ViewHolder {

        public BeaconVH(View itemView) {
            super(itemView);
        }

        public void bindView(int positon) {

        }
    }
}
