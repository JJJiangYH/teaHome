package com.hc.bluetoothlibrary.classicBluetooth;

import android.app.Activity;
import android.content.Context;

public class TaskThread {
    private final Context context;
    private WorkCallBack call;
    public TaskThread(Context context) {
        this.context = context;
    }

    public void setWorkCall(WorkCallBack call) {
        this.call = call;
        start();
    }

    private void start() {
        new Thread(() -> {
            if (call != null) {
                Activity activity = (Activity) context;
                final boolean b;
                try {
                    b = call.work();
                } catch (final Exception e) {
                    activity.runOnUiThread(() -> call.error(e));
                    e.printStackTrace();
                    return;
                }
                activity.runOnUiThread(() -> {
                    if (b) {
                        call.succeed();
                    }
                });
            }
        }).start();
    }

    public interface WorkCallBack {
        void succeed();

        boolean work() throws Exception;

        void error(Exception e);
    }
}