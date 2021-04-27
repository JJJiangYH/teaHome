package com.tea.view.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.preference.PreferenceManager;

import com.tea.view.View.Toast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-04-07 15:30
 */
public class DialogFragment extends androidx.fragment.app.DialogFragment {
    public static final String SETTING_MODE = "SETTING";
    public static final String LOGOUT_MODE = "LOGOUT";
    public static final String MARKDOWN_MODE = "MARKDOWN";
    static public final int MARKDOWN_SAVE = 1;
    static public final int MARKDOWN_DROP = 0;
    static public final int MARKDOWN_SHOW = 3;
    final String MODE;
    Handler handler;

    public DialogFragment(@Mode String MODE) {
        this(MODE, null);
    }

    public DialogFragment(@Mode String MODE, Handler handler) {
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
                                    .apply();
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
                break;
            case MARKDOWN_MODE:
                builder.setMessage("是否将未上传的文章保存到草稿箱")
                        .setPositiveButton("是",
                                (dialog, which) -> sendMessage(handler, MARKDOWN_SAVE))
                        .setNegativeButton("否",
                                (dialog, which) -> sendMessage(handler, MARKDOWN_DROP));
                break;
            default:
                break;
        }
        return builder.create();
    }

    private void sendMessage(Handler handler, int MODE) {
        if (handler != null) {
            Message message = Message.obtain(handler, MODE);
            message.obj = "Dialog";
            message.sendToTarget();
        } else {
            NullPointerException e =
                    new NullPointerException(this + "sendToTarget() handler is null");
            Log.e(this.toString(), e.getMessage());
        }
    }

    @Target(ElementType.PARAMETER)
    @StringDef({
            LOGOUT_MODE,
            MARKDOWN_MODE,
            SETTING_MODE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }
}