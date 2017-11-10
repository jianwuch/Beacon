package com.igrs.beacon.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.igrs.beacon.util.DialogManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    private int mLayoutId;
    private DialogManager mDialogManager;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dissmissProgress();
    }

    public void showProcess() {
        if (mDialogManager == null && null != getActivity()) {
            mDialogManager = new DialogManager(getActivity());
        }
        mDialogManager.showProgress();
    }

    public void dissmissProgress() {
        if (null != mDialogManager) {
            mDialogManager.dissmissDialog();
        }
    }
}
