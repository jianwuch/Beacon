package com.igrs.beacon.model.dm;

import android.bluetooth.BluetoothGatt;
import android.os.Handler;
import android.text.TextUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.igrs.beacon.config.AppConstans;
import com.igrs.beacon.model.data.BatchConfig;
import com.igrs.beacon.model.data.iBeacon;
import com.igrs.beacon.ui.ConfigurationActivity;
import com.igrs.beacon.util.HexIntUtil;
import com.igrs.beacon.util.LogUtil;
import com.igrs.beacon.util.ToastUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianw on 18-1-1.
 */

public class DeviceBatchBiz {
    private static final String TAG = DeviceBatchBiz.class.getSimpleName();
    private List<iBeacon> mDevices;
    private List<Integer> mFiledDeviceIndex;//批量失败的设备
    private List mFiledArguments;//批量失败的设备应该设置的参数保存在这里
    private BatchConfig config;

    //记录当前的修改配置，用于重试
    private String mCurrentType;//当前修改的类型寄存器
    private int mCurrentIndex;//当前修改的设备位置
    private boolean mCurrentIsConnect;//当前设备是否连接
    private String mNeedSetData;//需要设置的数据

    //设备重连次数记录
    private static int mConnectRetryCount = 0;
    private static int mWriteCount = 0;//写错误的次数记录

    private Handler mHandler;

    //取消操作
    public void cancel() {
        if (null != mHandler) {

            //移除所有的回调，防止内存泄漏
            mHandler.removeCallbacksAndMessages(null);
        }

        if (null != mFiledDeviceIndex) {
            mFiledDeviceIndex.clear();
        }
    }

    public void init(List mdatas, BatchConfig config) {
        this.mDevices = mdatas;
        mHandler = new Handler();
        this.config = config;
        mFiledDeviceIndex = new ArrayList();
        //开始遍历修改
        for (int i = 0; i < mDevices.size(); i++) {
            /**1.蓝牙连接
             * 2.密码输入校验
             * 3.开始修改其他参数**/

            mCurrentIndex = i;
            iBeacon currentDevice = mDevices.get(mCurrentIndex);
            connectDevice(currentDevice);
        }
    }

    //连接设备
    private void connectDevice(final iBeacon ibeacon) {
        BleManager.getInstance().connect(ibeacon.bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleException exception) {
                LogUtil.d(mCurrentType + "设备连接失败");
                if (mConnectRetryCount == 3) {
                    //重试三次，记录下来
                    mFiledDeviceIndex.add(mCurrentIndex);
                }
                mConnectRetryCount++;

                //重连
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d(mCurrentType + "重连第" + mConnectRetryCount + "次");
                        connectDevice(ibeacon);
                    }
                }, 100);
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                LogUtil.d(mCurrentType + "设备连接成功");
                mCurrentIsConnect = true;
                Notify();//订阅通知，来获取写入成功与否并进行下一项的修改
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device,
                    BluetoothGatt gatt, int status) {
                mCurrentIsConnect = false;

                //断开连接保存记录
                mFiledDeviceIndex.add(mCurrentIndex);
            }
        });
    }

    /**
     * 设置校验密码
     */
    private void setPassword(iBeacon device) {
        byte[] setPassword = HexUtil.hexStringToBytes(
                "57" + AppConstans.RegAD.PASSWORD + AppConstans.DEFAULT_PASSWORD);
        BleManager.getInstance()
                .write(device.bleDevice, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID, setPassword, new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {
                                LogUtil.d("写密码成功");
                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                LogUtil.d("写密码失败");
                            }
                        });
    }

    /**
     * 改minor
     * 直接输入10进制的数子
     */
    private void setMajor(iBeacon device, String value) {
        //int转16
        String hexData = HexIntUtil.decimalTo2ByteHex(Integer.parseInt(value));
        mCurrentType = AppConstans.RegAD.MAJOR;
        mNeedSetData = hexData;
        writeInfo(device, mCurrentType, value);
    }

    //改minor
    public void setMinor(iBeacon device, String value) {
        //int转16
        String hexData = HexIntUtil.decimalTo2ByteHex(Integer.parseInt(value));
        mCurrentType = AppConstans.RegAD.MINOR;
        mNeedSetData = hexData;
        writeInfo(device, mCurrentType, value);
    }

    //改名称
    public void setBleName(iBeacon device, String name) {
        if (TextUtils.isEmpty(name)) {
            LogUtil.e("输入的名称为空");
        }

        //转字节
        try {
            byte[] nameByte = name.getBytes("UTF-8");
            String nameHexStr = HexUtil.formatHexString(nameByte);//字节转16进制string
            mCurrentType = AppConstans.RegAD.BLE_NAME;
            mNeedSetData = nameHexStr;
            writeInfo(device, mCurrentType, mNeedSetData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setTxPower(iBeacon device, String value) {
        //int转16
        byte hexData = HexIntUtil.intTo1Byte(Integer.parseInt(value));
        String valueHexStr = HexUtil.formatHexString(new byte[] { hexData });
        mCurrentType = AppConstans.RegAD.TX_POWER;
        mNeedSetData = valueHexStr;
        writeInfo(device, mCurrentType, mNeedSetData);
    }

    private int new_bat;

    public void setBat(iBeacon device, String value) {
        //int转16
        new_bat = Integer.parseInt(value);
        byte hexData = HexIntUtil.intTo1Byte(new_bat);
        String valueHexStr = HexUtil.formatHexString(new byte[] { hexData });
        mCurrentType = AppConstans.RegAD.BAT;
        mNeedSetData = valueHexStr;
        writeInfo(device, mCurrentType, mNeedSetData);
    }

    public void setInterva(iBeacon device, String value) {
        //int转16
        String hexData = HexIntUtil.decimalTo2ByteHex(Integer.parseInt(value));
        mCurrentType = AppConstans.RegAD.INTERVAL;
        mNeedSetData = hexData;
        writeInfo(device, mCurrentType, mNeedSetData);
    }

    //写数据，修改参数
    private void writeInfo(iBeacon device, final String address, String data) {
        final String commStr = "57" + address + data;
        byte[] writeData = HexUtil.hexStringToBytes(commStr);
        BleManager.getInstance()
                .write(device.bleDevice, AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_WRITE_UUID, writeData, new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess() {
                                LogUtil.d(address + ":蓝牙写成功,等待notify结果:" + commStr);
                            }

                            @Override
                            public void onWriteFailure(BleException exception) {
                                LogUtil.d(address + ":蓝牙写失败:" + commStr);
                            }
                        });
    }

    /**
     * 订阅通知notify，成功之后先校验密码
     */
    private void Notify() {
        BleManager.getInstance()
                .notify(mDevices.get(mCurrentIndex), AppConstans.UUID_STR.SERVER_UUID,
                        AppConstans.UUID_STR.CHA_READ_UUID, new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                                // 打开通知操作成功（UI线程）
                                LogUtil.d("订阅通知数据成功,开始校验密码");
                                setPassword(mDevices.get(mCurrentIndex));
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

                                //操作类型，57：写/52读
                                byte type = data[0];

                                //操作的寄存器
                                byte address = data[1];

                                //一下是获取正在数据部分
                                int length = data.length - 2;
                                byte[] infoData = new byte[length];
                                System.arraycopy(data, 2, infoData, 0, length);

                                int addressInt = (int) (address);
                                if ((int) type != 87) {
                                    LogUtil.d("错误类型通知，当前只需要通知57--写");
                                    return;
                                }
                                LogUtil.d(addressInt + ":notiry写入成功");
                                switch (addressInt) {
                                    case 1://password
                                        LogUtil.d("密码验证成功");
                                        break;
                                    case 2://uuid
                                        setMajor(mDevices.get(mCurrentIndex), "" + (config.majorFrom
                                                + mCurrentIndex * config.majorStepLength));
                                        break;
                                    case 3://major
                                        setMinor(mDevices.get(mCurrentIndex), "" + (config.minorFrom
                                                + mCurrentIndex * config.minorStepLength));
                                        break;
                                    case 4://minor
                                        if (config.txPowerEnable) {
                                            setTxPower(mDevices.get(mCurrentIndex),
                                                    config.txPower + "");
                                        }
                                    case 5://tx_power
                                        if (config.nameEnable) {
                                            setTxPower(mDevices.get(mCurrentIndex), config.bleName);
                                        }
                                    case 6://ble_name
                                        if (config.batEnable) {
                                            setBat(mDevices.get(mCurrentIndex), config.bat + "");
                                        }
                                    case 7://bat
                                        if (config.intervalEnable) {
                                            setInterva(mDevices.get(mCurrentIndex),
                                                    config.interval + "");
                                        }
                                    case 8://interval
                                        //结束，开始下一个
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
    }
}
