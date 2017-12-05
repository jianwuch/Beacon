package com.igrs.beacon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianw on 17-12-4.
 */

public class FilterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.switch_uuid)
    Switch switchUuid;
    @BindView(R.id.switch_major)
    SwitchCompat switchMajor;
    @BindView(R.id.switch_minor)
    SwitchCompat switchMinor;
    @BindView(R.id.lay_uuid)
    LinearLayout layUuid;
    @BindView(R.id.lay_minor)
    LinearLayout layMinor;
    @BindView(R.id.ed_major_from)
    EditText edMajorFrom;
    @BindView(R.id.ed_major_to)
    EditText edMajorTo;
    @BindView(R.id.lay_major)
    LinearLayout layMajor;
    @BindView(R.id.ed_minor_from)
    EditText edMinorFrom;
    @BindView(R.id.ed_minor_to)
    EditText edMinorTo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initToolBar(toolBar, true, "过滤条件设置");
        initEvent();
    }

    private void initEvent() {
        switchUuid.setOnCheckedChangeListener(this);
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
}
