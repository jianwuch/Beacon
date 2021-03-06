package com.igrs.beacon.model.data;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import com.clj.fastble.data.BleDevice;
import com.igrs.beacon.util.iBeaconUtil;
import com.inuker.bluetooth.library.search.SearchResult;

/**
 * Created by jianw on 17-11-30.
 */

public class iBeacon extends BaseCheckable implements Parcelable {
    public BleDevice bleDevice;
    public String name;
    public int major;
    public int minor;
    public String proximityUuid;
    public String bluetoothAddress;
    public int txPower;
    public int rssi;

    public static iBeacon fromScanData(SearchResult searchResult) {
        return fromScanData(
                new BleDevice(searchResult.device, searchResult.rssi, searchResult.scanRecord, 0),
                searchResult.device, searchResult.rssi, searchResult.scanRecord);
    }

    public static iBeacon fromScanData(BleDevice bleDevice) {
        return fromScanData(bleDevice, bleDevice.getDevice(), bleDevice.getRssi(),
                bleDevice.getScanRecord());
    }

    public static iBeacon fromScanData(BleDevice bleDevice, BluetoothDevice device, int rssi,
            byte[] scanData) {

        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (((int) scanData[startByte + 2] & 0xff) == 0x02
                    && ((int) scanData[startByte + 3] & 0xff) == 0x15) {
                // yes!  This is an iBeacon
                patternFound = true;
                break;
            } else if (((int) scanData[startByte] & 0xff) == 0x2d
                    && ((int) scanData[startByte + 1] & 0xff) == 0x24
                    && ((int) scanData[startByte + 2] & 0xff) == 0xbf
                    && ((int) scanData[startByte + 3] & 0xff) == 0x16) {
                iBeacon iBeacon = new iBeacon();
                iBeacon.bleDevice = bleDevice;
                iBeacon.major = 0;
                iBeacon.minor = 0;
                iBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
                iBeacon.txPower = -55;
                return iBeacon;
            } else if (((int) scanData[startByte] & 0xff) == 0xad
                    && ((int) scanData[startByte + 1] & 0xff) == 0x77
                    && ((int) scanData[startByte + 2] & 0xff) == 0x00
                    && ((int) scanData[startByte + 3] & 0xff) == 0xc6) {

                iBeacon iBeacon = new iBeacon();
                iBeacon.bleDevice = bleDevice;
                iBeacon.major = 0;
                iBeacon.minor = 0;
                iBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
                iBeacon.txPower = -55;
                return iBeacon;
            }
            startByte++;
        }

        if (patternFound == false) {
            // This is not an iBeacon
            return null;
        }

        iBeacon iBeacon = new iBeacon();
        iBeacon.bleDevice = bleDevice;

        iBeacon.major =
                (scanData[startByte + 20] & 0xff) * 0x100 + (scanData[startByte + 21] & 0xff);
        iBeacon.minor =
                (scanData[startByte + 22] & 0xff) * 0x100 + (scanData[startByte + 23] & 0xff);
        iBeacon.txPower = (int) scanData[startByte + 24]; // this one is signed
        iBeacon.rssi = rssi;

        // AirLocate:
        // 02 01 1a 1a ff 4c 00 02 15  # Apple's fixed iBeacon advertising prefix
        // e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 # iBeacon profile uuid
        // 00 00 # major
        // 00 00 # minor
        // c5 # The 2's complement of the calibrated Tx Power

        // Estimote:
        // 02 01 1a 11 07 2d 24 bf 16
        // 394b31ba3f486415ab376e5c0f09457374696d6f7465426561636f6e00000000000000000000000000000000000000000000000000

        byte[] proximityUuidBytes = new byte[16];
        System.arraycopy(scanData, startByte + 4, proximityUuidBytes, 0, 16);
        String hexString = iBeaconUtil.bytesToHexString(proximityUuidBytes);
        StringBuilder sb = new StringBuilder();
        sb.append(hexString.substring(0, 8));
        sb.append("-");
        sb.append(hexString.substring(8, 12));
        sb.append("-");
        sb.append(hexString.substring(12, 16));
        sb.append("-");
        sb.append(hexString.substring(16, 20));
        sb.append("-");
        sb.append(hexString.substring(20, 32));
        iBeacon.proximityUuid = sb.toString();

        if (device != null) {
            iBeacon.bluetoothAddress = device.getAddress();
            iBeacon.name = device.getName();
        }

        return iBeacon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bleDevice, flags);
        dest.writeString(this.name);
        dest.writeInt(this.major);
        dest.writeInt(this.minor);
        dest.writeString(this.proximityUuid);
        dest.writeString(this.bluetoothAddress);
        dest.writeInt(this.txPower);
        dest.writeInt(this.rssi);
    }

    public iBeacon() {
    }

    protected iBeacon(Parcel in) {
        this.bleDevice = in.readParcelable(BleDevice.class.getClassLoader());
        this.name = in.readString();
        this.major = in.readInt();
        this.minor = in.readInt();
        this.proximityUuid = in.readString();
        this.bluetoothAddress = in.readString();
        this.txPower = in.readInt();
        this.rssi = in.readInt();
    }

    public static final Parcelable.Creator<iBeacon> CREATOR = new Parcelable.Creator<iBeacon>() {
        @Override
        public iBeacon createFromParcel(Parcel source) {
            return new iBeacon(source);
        }

        @Override
        public iBeacon[] newArray(int size) {
            return new iBeacon[size];
        }
    };
}
