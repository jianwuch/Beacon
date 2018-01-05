package com.igrs.beacon.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.model.data.BatchConfig;
import com.igrs.beacon.model.data.iBeacon;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianw on 18-1-1.
 * 批量配置界面
 */

public class BatchConfigationActivty extends BaseActivity{
    @BindView(R.id.tool_bar) Toolbar toolBar;
    @BindView(R.id.major_f) TextView majorF;
    @BindView(R.id.major_from) EditText majorFrom;
    @BindView(R.id.major_pear) EditText majorPear;
    @BindView(R.id.minor_from) EditText minorFrom;
    @BindView(R.id.minor_pear) EditText minorPear;
    @BindView(R.id.power) EditText power;
    @BindView(R.id.siwtch_tx_power) SwitchCompat siwtchTxPower;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.switch_name) SwitchCompat switchName;
    @BindView(R.id.interal) EditText interal;
    @BindView(R.id.siwtch_interval) SwitchCompat siwtchInterval;
    @BindView(R.id.start) Button start;
    private Object intentData;

    //数据部分
    private List<iBeacon> mDatas;

    //批量配置
    private BatchConfig mBatchConfig;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_batch_config);

        getIntentData();
        mBatchConfig = new BatchConfig();
    }

    public static void show(Context context, List<iBeacon> dats) {
        Intent intent = new Intent(context, BatchConfigationActivty.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(INTENT_KEY, (ArrayList<? extends Parcelable>) dats);
        intent.putExtra(INTENT_KEY, bundle);
        context.startActivity(intent);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(INTENT_KEY);

        this.mDatas = bundle.getParcelableArrayList(INTENT_KEY);
        if (null != mDatas || mDatas.size() > 0) {
            toolBar.setSubtitle("选中（" + mDatas.size() + "）设备");
        }
    }

    @OnClick(R.id.start)
    public void start() {

        //获取配置选项
        //开始批量配置
        for (int i = 0; i < mDatas.size(); i++) {

        }
    }

    //
    @OnCheckedChanged({R.id.siwtch_tx_power,R.id.switch_name, R.id.siwtch_interval})
    public void onCheckedChanged(SwitchCompat view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.switch_name:
                name.setEnabled(isChecked);
                mBatchConfig.nameEnable = isChecked;
                break;
            case R.id.siwtch_tx_power:
                power.setEnabled(isChecked);
                mBatchConfig.txPowerEnable = isChecked;
                break;
            case R.id.siwtch_interval:
                interal.setEnabled(isChecked);
                mBatchConfig.intervalEnable = isChecked;
                break;
        }
    }
}
