package com.igrs.beacon.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.igrs.beacon.R;
import com.igrs.beacon.model.data.ScanLeDevice;
import java.util.List;

/**
 * Created by jove.chen on 2017/10/31.
 */

public class ScanDevicesAdapter extends RecyclerView.Adapter<ScanDevicesAdapter.DevicesVH> {

    private List<ScanLeDevice> mDatas;

    public ScanDevicesAdapter(List<ScanLeDevice> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public DevicesVH onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DevicesVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_device_info, parent, false));
    }

    @Override
    public void onBindViewHolder(DevicesVH holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class DevicesVH extends RecyclerView.ViewHolder {
        @BindView(R.id.device_info) TextView deviceInfo;

        DevicesVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int position) {
            ScanLeDevice onDevices = mDatas.get(position);
            deviceInfo.setText(onDevices.toString());
        }
    }
}
