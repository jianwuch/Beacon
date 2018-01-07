package com.igrs.beacon.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.model.data.BatchConfig;
import com.igrs.beacon.model.data.iBeacon;
import com.igrs.beacon.model.dm.DeviceBatchBiz;
import com.igrs.beacon.util.ToastUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
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
    @BindView(R.id.uuid)
    EditText uuid;
    @BindView(R.id.switch_uuid)
    SwitchCompat siwtchUuid;
    @BindView(R.id.power)
    EditText power;
    @BindView(R.id.siwtch_tx_power)
    SwitchCompat siwtchTxPower;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.switch_name)
    SwitchCompat switchName;
    @BindView(R.id.bat)
    EditText bat;
    @BindView(R.id.siwtch_bat)
    SwitchCompat siwtchBat;
    @BindView(R.id.interal)
    EditText interal;
    @BindView(R.id.siwtch_interval)
    SwitchCompat siwtchInterval;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.process)
    ProgressBar processBar;
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

        String majorStr = majorFrom.getText().toString().trim();
        String minorStr = minorFrom.getText().toString().trim();
        String majorStepLen = majorPear.getText().toString().trim();
        String minorStepLen = minorPear.getText().toString().trim();

        if (TextUtils.isEmpty(majorStr) || TextUtils.isEmpty(minorStr) || TextUtils.isEmpty(majorStepLen) ||
                TextUtils.isEmpty(minorStepLen)) {
            ToastUtil.ToastShort(this, "major,minor参数必填");
            return;
        } else {
            mBatchConfig.majorFrom = Integer.parseInt(majorStr);
            mBatchConfig.minorFrom = Integer.parseInt(minorStr);
            mBatchConfig.majorStepLength = Integer.parseInt(majorStepLen);
            mBatchConfig.minorStepLength = Integer.parseInt(minorStepLen);

            //校验数据
            if(mBatchConfig.majorFrom >=65532 || mBatchConfig.minorFrom >= 65532) {
                ToastUtil.ToastShort(this, "marjor/minor数据超过范围");
                return;
            }

            if (mBatchConfig.minorFrom + mBatchConfig.minorStepLength*mDatas.size() >= 65532||
                    mBatchConfig.majorFrom + mBatchConfig.majorStepLength*mDatas.size() >= 65532) {
                ToastUtil.ToastShort(this, "步长设置错误，已超过最大限制值");
                return;
            }
        }

        if (mBatchConfig.uuidEnable) {
            String uuidStr = uuid.getText().toString().trim();
            if (TextUtils.isEmpty(uuidStr)) {
                ToastUtil.ToastShort(this, "打开了UUID就需要选择UUID");
                return;
            } else {
                mBatchConfig.uuid = uuidStr;
            }

        }
        if (mBatchConfig.intervalEnable) {
            String intervalStr = interal.getText().toString().trim();
            if (TextUtils.isEmpty(intervalStr)) {
                ToastUtil.ToastShort(this, "打开了interval就需要必填");
                return;
            } else {
                mBatchConfig.interval = Integer.parseInt(intervalStr);
            }
        }
        if (mBatchConfig.txPowerEnable) {
            String txPowerStr = power.getText().toString().trim();
            if (TextUtils.isEmpty(txPowerStr)) {
                ToastUtil.ToastShort(this, "打开了txPower就需要必填");
                return;
            } else {
                mBatchConfig.txPower = Integer.parseInt(txPowerStr);
            }

        }
        if (mBatchConfig.nameEnable) {
            String nameStr = name.getText().toString().trim();
            if (TextUtils.isEmpty(nameStr)) {
                ToastUtil.ToastShort(this, "打开了Ble name就需要必填");
                return;
            } else {
                mBatchConfig.bleName = nameStr;
            }
        }
        if (mBatchConfig.batEnable) {
            String batStr = bat.getText().toString().trim();
            if (TextUtils.isEmpty(batStr)) {
                ToastUtil.ToastShort(this, "打开了Bat就需要必填");
                return;
            } else {
                mBatchConfig.bat = Integer.parseInt(batStr);
            }
        }

        DeviceBatchBiz deviceBatchBiz = new DeviceBatchBiz();
        deviceBatchBiz.setProcessChangedLinstener(new DeviceBatchBiz.ProcessChangedLinstener() {
            @Override
            public void onChanged(int process, String processStr) {
                result.setText(String.format("处理结果：（%1$s）", processStr));
                processBar.setProgress(process);
            }

            @Override
            public void onFinished() {
                result.setText("处理完成");
            }
        });

        //开始配置
        deviceBatchBiz.beginBatch(mDatas, mBatchConfig);
    }

    //
    @OnCheckedChanged({R.id.siwtch_tx_power, R.id.switch_name, R.id.switch_uuid, R.id.siwtch_bat, R.id.siwtch_interval})
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
            case R.id.switch_uuid:
                uuid.setEnabled(isChecked);
                mBatchConfig.uuidEnable = isChecked;
                break;
            case R.id.siwtch_bat:
                bat.setEnabled(isChecked);
                mBatchConfig.batEnable = isChecked;
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.uuid)
    public void selectUUID() {
        UUIDManagerActivity.show(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String uuidStr = UUIDManagerActivity.getResult(this, requestCode,resultCode, data);
        if (TextUtils.isEmpty(uuidStr)) return;
        uuid.setText(uuidStr);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
