package com.igrs.beacon.ui;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.config.AppConstans;
import com.igrs.beacon.util.LogUtil;
import com.igrs.beacon.util.ToastUtil;

import butterknife.BindView;

/**
 * Created by jove.chen on 2017/12/12.
 * 单个蓝牙的配置界面
 */

public class ConfigurationActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.uuid)
    TextView uuid;
    @BindView(R.id.lay_uuid)
    LinearLayout layUuid;
    @BindView(R.id.major)
    EditText major;
    @BindView(R.id.minor)
    EditText minor;
    @BindView(R.id.measure_power)
    EditText measurePower;
    @BindView(R.id.tx_power)
    EditText txPower;
    @BindView(R.id.tx_time)
    EditText txTime;
    @BindView(R.id.device_name)
    EditText deviceName;
    private BleDevice device;

    public static void show(Context context, BleDevice device) {
        Intent intent = new Intent(context, ConfigurationActivity.class);
        intent.putExtra(INTENT_KEY, device);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        initToolBar(toolBar, true, "修改参数");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDeviceFromIntent();

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
                LogUtil.d("蓝牙连接成功");

                Notify();
                //读参数
  /*              BluetoothGatt gatt1 = BleManager.getInstance().getBluetoothGatt(bleDevice);
                for (BluetoothGattService service : gatt.getServices()) {
                    LogUtil.d("Server:" + String.valueOf(service.getUuid()));//server

                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        LogUtil.d("Characteristic:" + characteristic.getUuid() + "\nPermission:" + characteristic.getProperties());
                    }
                }*/
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device,
                                       BluetoothGatt gatt, int status) {
                showLoading(false);
                ToastUtil.ToastShort(ConfigurationActivity.this, "断开连接");
            }
        });
    }

    private void getDeviceFromIntent() {
        Intent intent = getIntent();
        device = intent.getParcelableExtra(INTENT_KEY);
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

    private void readInfo() {
         byte[] setPassword = HexUtil.hexStringToBytes("57" + AppConstans.RegAD.PASSWORD + AppConstans.DEFAULT_PASSWORD);
        BleManager.getInstance()
                .write(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID,setPassword
                        ,
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {
                                LogUtil.d("写密码成功");
                                getDeviceName();
                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                LogUtil.d("写密码失败");
                            }
                        });
    }

    private void getDeviceName() {
        BleManager.getInstance().write(device, AppConstans.UUID_STR.SERVER_UUID, AppConstans.UUID_STR.CHA_WRITE_UUID,
                HexUtil.hexStringToBytes("52" + AppConstans.RegAD.BLE_NAME), new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess() {
                        LogUtil.d("写读取名称成功");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        LogUtil.d("写读取名称失败");
                    }
                });
    }

    private void Notify() {
        BleManager.getInstance().notify(
                device,
                AppConstans.UUID_STR.SERVER_UUID,
                AppConstans.UUID_STR.CHA_READ_UUID,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        // 打开通知操作成功（UI线程）
                        LogUtil.d("订阅通知数据成功");
                        readInfo();
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        // 打开通知操作失败（UI线程）
                        LogUtil.d("订阅通知数据失败");
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现（UI线程）
                        LogUtil.d("Notify通知数据："+HexUtil.formatHexString(data));
                    }
                });
    }
}
