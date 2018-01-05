package com.igrs.beacon.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseFragment;
import com.igrs.beacon.model.data.ScanLeDevice;
import com.igrs.beacon.model.dm.DeviceAddStrategy;
import com.igrs.beacon.model.dm.MACDeviceAddStrategy;
import com.igrs.beacon.ui.adapter.ScanDevicesAdapter;
import com.igrs.beacon.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConnectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectFragment extends BaseFragment {
    private static final String TAG = ConnectFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SCAN_PERIOD = 10000;
    Unbinder unbinder;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycle_View) RecyclerView mRecycleView;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private BluetoothAdapter mBluetoothAdapter;
    private List<ScanLeDevice> mDevices;
    private ScanDevicesAdapter mAdapter;
    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    private DeviceAddStrategy mDeviceAddStrategy;

    //LeScanCallback 是蓝牙扫描返回结果的回调，可以通过回调获取扫描结果。
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    Log.d(TAG, device.getAddress());
                    Log.d(TAG, Thread.currentThread().getName());
                    if (mDeviceAddStrategy == null) {
                        mDeviceAddStrategy = new MACDeviceAddStrategy();
                    }

                    if (mDeviceAddStrategy.add(mDevices, new ScanLeDevice(device, rssi, scanRecord))) {
                        mAdapter.notifyDataSetChanged();
                    }
                    //成功扫描到设备后，在这里获得bluetoothDevice。可以放进设备列表成员变量当中方便后续操作。
                    //也可以发广播通知activity发现了新设备，更新活动设备列表的显示等。
                    //这里需要注意一点，在onLeScan当中不能执行耗时操作，不宜执行复杂运算操作，切记，
                    //下面即将提到的onScanResult，onBatchScanResults同理。
                }
            };
    private boolean mScanning;//当前是否还在扫描蓝牙

    public ConnectFragment() {
        // Required empty public constructor
    }

    public static ConnectFragment newInstance(String param1, String param2) {
        ConnectFragment fragment = new ConnectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        unbinder = ButterKnife.bind(this, view);
        initEvent();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scanLeDevice(true);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showProcess();
        mHandler = new Handler();
        mDevices = new ArrayList<>();
        mAdapter = new ScanDevicesAdapter(mDevices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(
                new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        mRecycleView.setAdapter(mAdapter);
        initBluetooth();
    }

    private void initBluetooth() {
        //1.检查是否支持ble
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtil.ToastShort(getActivity(), "手机不支持BLE功能");
            return;
        }
        //2.获取去蓝牙适配器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager =
                    (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        //3.0检查蓝牙是否打开
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //4.0开始扫描设备
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            showProcess();
            swipeRefreshLayout.setRefreshing(true);
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //mScanning = false;
                    //mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    scanLeDevice(false);
                    swipeRefreshLayout.setRefreshing(false);
                    //invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            dissmissProgress();
        }
        //invalidateOptionsMenu();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    ToastUtil.ToastShort(getActivity(), "蓝牙已打开");
                    initBluetooth();
                    break;
            }
        } else {
            ToastUtil.ToastShort(getActivity(), "蓝牙未打开，无法使用");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
