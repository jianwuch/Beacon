package com.igrs.beacon.ui;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.config.AppConstans;
import com.igrs.beacon.moudle.data.iBeacon;
import com.igrs.beacon.util.ToastUtil;

/**
 * Created by jove.chen on 2017/12/12.
 * 单个蓝牙的配置界面
 */

public class ConfigurationActivity extends BaseActivity {
    @BindView(R.id.tool_bar) Toolbar toolBar;
    @BindView(R.id.uuid) TextView uuid;
    @BindView(R.id.lay_uuid) LinearLayout layUuid;
    @BindView(R.id.major) EditText major;
    @BindView(R.id.minor) EditText minor;
    @BindView(R.id.measure_power) EditText measurePower;
    @BindView(R.id.tx_power) EditText txPower;
    @BindView(R.id.tx_time) EditText txTime;
    @BindView(R.id.device_name) EditText deviceName;
    private BleDevice device;

    public static void show(Context context, BleDevice device) {
        Intent intent = new Intent(context, ConfigurationActivity.class);
        intent.putExtra(INTENT_KEY, device);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
            @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);

        initToolBar(toolBar, true, "修改参数");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //链接设备
        BleManager.getInstance().connect(device, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                showLoading(true);
            }

            @Override
            public void onConnectFail(BleException exception) {
                showLoading(false);
                ToastUtil.ToastShort(ConfigurationActivity.this, "连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                showLoading(false);
                ToastUtil.ToastShort(ConfigurationActivity.this, "连接成功");
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device,
                    BluetoothGatt gatt, int status) {
                showLoading(false);
                ToastUtil.ToastShort(ConfigurationActivity.this, "断开连接");
            }
        });
        //读参数
        //readInfo();
    }

    private void readInfo() {
        BleManager.getInstance()
                .write(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID,
                        HexUtil.hexStringToBytes("0x57" + AppConstans.RegAD.PASSWORD + "123456"),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {

                            }

                            @Override
                            public void onWriteFailure(BleException exception) {

                            }
                        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.configuration_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //保存参数
    private void save() {

    }
}
