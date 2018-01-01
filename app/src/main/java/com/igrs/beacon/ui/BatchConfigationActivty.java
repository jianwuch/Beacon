package com.igrs.beacon.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.moudle.data.iBeacon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianw on 18-1-1.
 * 批量配置界面
 */

public class BatchConfigationActivty extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.major_from)
    EditText majorFrom;
    @BindView(R.id.major_pear)
    EditText majorPear;
    @BindView(R.id.minor_from)
    EditText minorFrom;
    @BindView(R.id.minor_pear)
    EditText minorPear;
    @BindView(R.id.tx_power)
    EditText txPower;
    @BindView(R.id.siwtch_tx_power)
    SwitchCompat siwtchTxPower;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.switch_name)
    SwitchCompat switchName;
    @BindView(R.id.interal)
    EditText interal;
    @BindView(R.id.siwtch_interval)
    SwitchCompat siwtchInterval;
    @BindView(R.id.start)
    Button startBtn;
    private Object intentData;
    private List<iBeacon> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_batch_config);

        getIntentData();
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
        //开始批量配置
    }
}
