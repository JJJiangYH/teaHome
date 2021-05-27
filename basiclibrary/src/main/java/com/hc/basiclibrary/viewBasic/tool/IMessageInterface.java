package com.hc.basiclibrary.viewBasic.tool;

import android.os.Handler;

public interface IMessageInterface {
    void setHandler(Handler handler);
    void readData(Object o, byte[] data);
    void updateState(int state);
}
