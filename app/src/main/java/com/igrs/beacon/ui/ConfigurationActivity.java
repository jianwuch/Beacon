package com.igrs.beacon.ui;

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
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.moudle.data.iBeacon;

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
    private iBeacon device;

    public static void show(Context context, iBeacon device) {
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



        //读参数
        readInfo();
    }

    private void readInfo() {
        //BleManager.getInstance().write();
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
