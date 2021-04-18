package com.tea.teahome.Setting.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.tea.teahome.Widget.Toast;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-04-07 15:30
 */
public class MyDialogFragment extends DialogFragment {
    public static final String SETTING_MODE = "SETTING";
    public static final String LOGOUT_MODE = "LOGOUT";
    final String MODE;
    Handler handler;

    public MyDialogFragment(String MODE) {
        this(MODE, null);
    }

    public MyDialogFragment(String MODE, Handler handler) {
        this.MODE = MODE;
        if (handler != null) {
            this.handler = handler;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        switch (MODE) {
            case SETTING_MODE:
                builder.setMessage("恢复默认设置")
                        .setPositiveButton("确定", (dialog, which) -> {
                            PreferenceManager
                                    .getDefaultSharedPreferences(getContext())
                                    .edit()
                                    .clear()
                                    .commit();
                            Toast.getToast(getActivity(), "已恢复默认设置").show();
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                        });
                break;
            case LOGOUT_MODE:
                builder.setMessage("是否退出账号")
                        .setPositiveButton("确定", (dialog, which) -> {
                            if (handler != null) {
                                Message m = handler.obtainMessage(1);
                                handler.sendMessage(m);
                            }
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                        });
        }
        return builder.create();
    }
}