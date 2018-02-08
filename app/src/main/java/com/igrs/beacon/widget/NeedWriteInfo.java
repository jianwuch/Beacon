package com.igrs.beacon.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.igrs.beacon.R;
import java.io.InterruptedIOException;

/**
 * Created by jove.chen on 2018/2/8.
 */

public class NeedWriteInfo extends FrameLayout implements View.OnClickListener {
    private View btn_cancel;
    private TextView textView;
    private CancelListener mCancelListener;

    public void setCancelListener(CancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public NeedWriteInfo(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NeedWriteInfo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NeedWriteInfo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView =
                LayoutInflater.from(context).inflate(R.layout.need_write_info_layout, this, true);

        btn_cancel = rootView.findViewById(R.id.btn_cancel);
        textView = rootView.findViewById(R.id.result);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (mCancelListener != null) {
                    mCancelListener.onCancel();
                }
                ((ViewGroup) getParent()).removeView(this);

                break;
        }
    }

    public void setReult(String result) {
        textView.setText(result);
    }

    public interface CancelListener {
        void onCancel();
    }

    public void removeSelf() {
        btn_cancel.performClick();
    }
}
