package com.hc.mixthebluetooth.fragment;

import android.os.Handler;
import android.os.Message;

import com.hc.basiclibrary.viewBasic.tool.IMessageInterface;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

public class FragmentMessage implements IMessageInterface {
    public static final int DATA_TO_MODULE = 0x03;

    private Handler mHandler;
    private DeviceModule module;

    @Override
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void readData(Object o, byte[] data) {
        if (module == null) {
            module = (DeviceModule) o;
        }
    }

    @Override
    public void updateState(int state) {
    }

    public void setSendData(String s) {
        sendData(new FragmentMessageItem(false, Analysis.getBytes(s, false), null, module));
    }

    private void sendData(FragmentMessageItem item) {
        if (mHandler == null)
            return;
        Message message = mHandler.obtainMessage();
        message.what = DATA_TO_MODULE;
        message.obj = item;
        mHandler.sendMessage(message);
    }
}