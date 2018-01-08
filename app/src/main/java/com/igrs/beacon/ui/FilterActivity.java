package com.igrs.beacon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.OnClick;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.config.FilterManager;
import com.igrs.beacon.model.data.FilterConfig;

import butterknife.BindView;
import com.igrs.beacon.util.ToastUtil;

/**
 * Created by jianw on 17-12-4.
 */

public class FilterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    public static final int REQUEST_CODE = 201;
    @BindView(R.id.tool_bar) Toolbar toolBar;
    @BindView(R.id.switch_uuid) Switch switchUuid;
    @BindView(R.id.switch_major) SwitchCompat switchMajor;
    @BindView(R.id.switch_minor) SwitchCompat switchMinor;
    @BindView(R.id.lay_uuid) LinearLayout layUuid;
    @BindView(R.id.lay_minor) LinearLayout layMinor;
    @BindView(R.id.ed_major_from) EditText edMajorFrom;
    @BindView(R.id.ed_major_to) EditText edMajorTo;
    @BindView(R.id.lay_major) LinearLayout layMajor;
    @BindView(R.id.ed_minor_from) EditText edMinorFrom;
    @BindView(R.id.ed_minor_to) EditText edMinorTo;
    @BindView(R.id.tv_uuid) TextView tvUuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initToolBar(toolBar, true, "过滤条件设置");
        initEvent();
        showFilterConfig();
    }

    private void showFilterConfig() {
        FilterConfig config = FilterManager.getFilterConfig();
        switchUuid.setChecked(config.enableUUID);
        switchMajor.setChecked(config.enableMajor);
        switchMajor.setChecked(config.enableMinor);

        if (config.enableUUID) {
            tvUuid.setText(config.filterUUID);
        }

        if (config.enableMajor) {
            edMajorFrom.setText(config.majorFrom);
            edMajorTo.setText(config.majorTo);
        }

        if (config.enableMinor) {
            edMinorFrom.setText(config.majorFrom);
            edMinorTo.setText(config.majorTo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UUIDManagerActivity.REQUEST_RESULT:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        ToastUtil.ToastShort(this, "未选择uuid");
                        break;
                    case RESULT_OK:
                        tvUuid.setText(data.getStringExtra(UUIDManagerActivity.UUID_KEY));
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initEvent() {
        switchUuid.setOnCheckedChangeListener(this);
        switchMajor.setOnCheckedChangeListener(this);
        switchMinor.setOnCheckedChangeListener(this);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterConfig config = new FilterConfig();
                config.enableUUID = switchUuid.isChecked();
                config.enableMajor = switchMajor.isChecked();
                config.enableMinor = switchMinor.isChecked();
                if (config.enableUUID) {
                    config.filterUUID = tvUuid.getText().toString().trim();
                    if (TextUtils.isEmpty(config.filterUUID)) {
                        ToastUtil.ToastShort(FilterActivity.this, "开启了UUID过滤需要选择一个过滤的uuid");
                        return;
                    }
                }

                if (config.enableMajor) {
                    String majorF = edMajorFrom.getText().toString().trim();
                    String majorT = edMajorTo.getText().toString().trim();

                    if (TextUtils.isEmpty(majorF) || TextUtils.isEmpty(majorT)) {
                        ToastUtil.ToastShort(FilterActivity.this, "开启了major过滤需要填写major");
                        return;
                    }
                    config.majorFrom = Integer.parseInt(majorF);
                    config.majorTo = Integer.parseInt(majorT);
                }

                if (config.enableMinor) {
                    String mimorF = edMinorFrom.getText().toString().trim();
                    String mimorT = edMinorTo.getText().toString().trim();

                    if (TextUtils.isEmpty(mimorF) || TextUtils.isEmpty(mimorT)) {
                        ToastUtil.ToastShort(FilterActivity.this, "开启了minor过滤需要填写minor");
                        return;
                    }
                    config.minorFrom = Integer.parseInt(mimorF);
                    config.minorTo = Integer.parseInt(mimorT);
                }

                FilterManager.saveNewConfig(config);

                //通知页面过滤选择情况
                Intent intent = new Intent();
                intent.putExtra(INTENT_RESULT_KEY, config);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_uuid:
                layUuid.setVisibility(b ? View.VISIBLE : View.GONE);
                break;

            case R.id.switch_major:
                layMajor.setVisibility(b ? View.VISIBLE : View.GONE);
                break;

            case R.id.switch_minor:
                layMinor.setVisibility(b ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @OnClick(R.id.btn_select)
    public void select() {
        UUIDManagerActivity.show(this);
    }
}
