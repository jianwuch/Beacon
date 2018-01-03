package com.igrs.beacon.ui;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.igrs.beacon.util.HexIntUtil;
import com.igrs.beacon.util.LogUtil;
import com.igrs.beacon.util.ToastUtil;

import butterknife.BindView;

/**
 * Created by jove.chen on 2017/12/12.
 * 单个蓝牙的配置界面
 */

public class ConfigurationActivity extends BaseActivity {
    public static final int WRITE = 87;
    public static final int READ = 82;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.uuid)
    EditText uuid;
    @BindView(R.id.major)
    EditText major;
    @BindView(R.id.minor)
    EditText minor;
    @BindView(R.id.tx_power)
    EditText txPower;
    @BindView(R.id.ble_name)
    EditText bleName;
    @BindView(R.id.bat)
    EditText bat;
    @BindView(R.id.interval)
    EditText interval;

    @BindView(R.id.status)
    TextView statusTextView;
    private BleDevice device;

    private int pre_major;
    private int pre_minor;
    private String pre_name;
    private int pre_interval;
    private boolean isConnected;
    private Handler mHandler;

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
        mHandler = new Handler();

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
                isConnected = false;

                //todo 重连机制
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                showLoading(false);
                LogUtil.d("蓝牙连接成功");
                isConnected = true;
                statusTextView.setText(String.format(getString(R.string.status), "已连接"));
                Notify();//开启通知
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
                isConnected = false;
                statusTextView.setText(String.format(getString(R.string.status), "已断开"));
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
        String majorStr = major.getText().toString().trim();
        String minorStr = minor.getText().toString().trim();
        String new_name = bleName.getText().toString().trim();
        String intervalStr = interval.getText().toString().trim();

        //major
        if (!TextUtils.isEmpty(majorStr)) {
            int new_major = Integer.parseInt(majorStr);
            if (pre_major == new_major) {
                //不需要修改
            } else {
                //修改major
            }
        }

        //minor
        if (!TextUtils.isEmpty(minorStr)) {
            int new_minor = Integer.parseInt(minorStr);
            if (pre_minor == new_minor) {
                //不需要修改
            } else {
                //修改minor
            }
        }

        if (!TextUtils.isEmpty(new_name) && !pre_name.equals(new_name)) {
            //修改name
        }
    }

    private void setPassword() {
        byte[] setPassword = HexUtil.hexStringToBytes(
                "57" + AppConstans.RegAD.PASSWORD + AppConstans.DEFAULT_PASSWORD);
        BleManager.getInstance()
                .write(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID, setPassword, new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {
                                LogUtil.d("写密码成功");
                                //成功之后在notiry中去干其他操作
                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                LogUtil.d("写密码失败");
                            }
                        });
    }

    //写数据，修改参数
    private void writeInfo(final String address, String data) {
        String commStr = "57" + address + data;
        byte[] writeData = HexUtil.hexStringToBytes(commStr);
        BleManager.getInstance().write(device, AppConstans.UUID_STR.SERVER_UUID,
                AppConstans.UUID_STR.CHA_WRITE_UUID, writeData, new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess() {
                        LogUtil.d(address+":蓝牙写成功,等待notify结果");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        LogUtil.d(address+":蓝牙写失败");
                    }
                });
    }

    private void getDeviceName() {
        BleManager.getInstance()
                .write(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID,
                        HexUtil.hexStringToBytes("52" + AppConstans.RegAD.BLE_NAME),
                        new BleWriteCallback() {
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



    //一次性拿全部属性
    static int address = 2;
    static int error_count = 0;
    private void getAllInfo() {
        if (address >= 9) return;
        LogUtil.d("开始读取寄存器数据："+address);
        if (error_count >=3) {
            ToastUtil.ToastShort(this, "重试3次读取失败");
            return;
        }
        final String addressStr = String.format("0%1$d", address);
        BleManager.getInstance()
                .write(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID,
                        HexUtil.hexStringToBytes("52" + addressStr),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {
                                LogUtil.d(addressStr + "写读取名称成功");
                                address++;
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAllInfo();
                                    }
                                }, 100);//延时下一个蓝牙的操作，因为不延时已经出现串notify的情况
                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                LogUtil.d(addressStr + "写读取名称失败");
                                error_count++;
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getAllInfo();
                                    }
                                }, 100);
                            }
                        });
    }

    private void getAddressInfo(final String addressStr) {
        BleManager.getInstance()
                .write(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID,
                        HexUtil.hexStringToBytes("52" + addressStr),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {
                                LogUtil.d(addressStr + "写读取名称成功");
                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                LogUtil.d(addressStr + "写读取名称失败");
                            }
                        });
    }

    private void Notify() {
        BleManager.getInstance()
                .notify(device, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_READ_UUID, new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                                // 打开通知操作成功（UI线程）
                                LogUtil.d("订阅通知数据成功");
                                setPassword();
                            }

                            @Override
                            public void onNotifyFailure(BleException exception) {
                                // 打开通知操作失败（UI线程）
                                LogUtil.d("订阅通知数据失败");
                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {
                                // 打开通知后，设备发过来的数据将在这里出现（UI线程）
                                LogUtil.d("Notify通知数据：" + HexUtil.formatHexString(data));
                                ToastUtil.ToastShort(ConfigurationActivity.this,
                                        HexUtil.formatHexString(data));

                                //操作类型，57：写/52读
                                byte type = data[0];

                                //操作的寄存器
                                byte address = data[1];

                                //一下是获取正在数据部分
                                int length = data.length - 2;
                                byte[] infoData = new byte[length];
                                System.arraycopy(data, 2, infoData, 0, length);

                                int addressInt = (int)(address);
                                switch ((int)type) {
                                    case WRITE:
                                        if (infoData.length == 1 && ((int)infoData[0]) == 0) {
                                            LogUtil.d(addressInt +":notiry写入失败");

                                            if (addressInt == 1 && isConnected) {

                                                //重新写入密码
                                                LogUtil.d("密码重新登入");
                                                setPassword();
                                            }
                                            return;
                                        }

                                        LogUtil.d(addressInt +":notiry写入成功");
                                        switch (addressInt) {
                                            case 1://password
                                                LogUtil.d("密码验证成功");
                                                getAllInfo();//密码输出正确之后开始获取其他数据
                                                break;
                                            case 2://uuid
                                                uuid.setText(HexUtil.encodeHexStr(infoData));
                                                break;
                                            case 3://major
                                                major.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                            case 4://minor
                                                minor.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                            case 5:
                                                txPower.setText(HexIntUtil.getInt(infoData, false)+"");
                                                break;
                                            case 6://ble_name
                                                bleName.setText("");
                                                break;

                                            case 7://bat
                                                bat.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;

                                            case 8://interval
                                                interval.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                        }
                                        break;

                                    case READ:
                                        if (infoData.length == 1 && ((int)infoData[0]) == 0) {
                                            ToastUtil.ToastShort(ConfigurationActivity.this,
                                                    "读取失败");
                                            return;
                                        }
                                        switch (HexIntUtil.getInt(new byte[]{address}, false)) {
                                            case 1://password
                                                password.setText(HexUtil.encodeHexStr(infoData));
                                                break;
                                            case 2://uuid
                                                uuid.setText(HexUtil.encodeHexStr(infoData));
                                                break;
                                            case 3://major
                                                major.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                            case 4://minor
                                                minor.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                            case 5://tx_power
                                                txPower.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                            case 6://ble_name
                                                bleName.setText(new String(infoData));
                                                break;

                                            case 7://bat
                                                bat.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;

                                            case 8://interval
                                                interval.setText(HexIntUtil.getInt(infoData, false) + "");
                                                break;
                                        }
                                        break;
                                }
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnect(device);
        address = 2;
    }
}
