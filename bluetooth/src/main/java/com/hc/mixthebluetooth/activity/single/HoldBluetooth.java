package com.hc.mixthebluetooth.activity.single;

import android.content.Context;
import android.util.Log;

import com.hc.bluetoothlibrary.AllBluetoothManage;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.bluetoothlibrary.IBluetooth;

import java.util.ArrayList;
import java.util.List;

public class HoldBluetooth {
    private static final HoldBluetooth ourInstance = new HoldBluetooth();
    private final List<DeviceModule> mConnectedArray = new ArrayList<>();//成功连接了的模块（可扩展多连接）
    private AllBluetoothManage mAllBluetoothManage;
    private OnReadDataListener mDataListener;

    private HoldBluetooth() {
    }

    public static HoldBluetooth getInstance() {
        return ourInstance;
    }

    public void initHoldBluetooth(final Context context, final UpdateList updateList) {

        //接口里都有注释
        mAllBluetoothManage = new AllBluetoothManage(context, new IBluetooth() {
            @Override
            public void updateList(DeviceModule deviceModule) {
                if (updateList != null)
                    updateList.update(true, deviceModule);
            }

            @Override
            public void connectSucceed(DeviceModule deviceModule) {
                mConnectedArray.clear();//多连接的话这里要优化
                mConnectedArray.add(deviceModule);
                if (mDataListener != null)
                    mDataListener.connectSucceed();
                log("连接成功; " + deviceModule.getName());
            }

            @Override
            public void updateEnd() {
                if (updateList != null)
                    updateList.update(false, null);
            }

            @Override
            public void updateMessyCode(DeviceModule deviceModule) {
                if (updateList != null)
                    updateList.updateMessyCode(true, deviceModule);
            }

            @Override
            public void readData(String mac, byte[] data) {
                if (mDataListener != null)
                    mDataListener.readData(mac, data);
            }

            @Override
            public void reading(boolean isStart) {
                if (mDataListener != null)
                    mDataListener.reading(isStart);
            }

            @Override
            public void errorDisconnect(DeviceModule deviceModule) {
                if (mDataListener != null)
                    mDataListener.errorDisconnect(deviceModule);
            }

            @Override
            public void readNumber(int number) {
                if (mDataListener != null)
                    mDataListener.readNumber(number);
            }

            @Override
            public void readLog(String className, String data, String lv) {
            }
        });
    }

    //刷新
    public boolean scan() {
        return mAllBluetoothManage.mixScan();
    }

    //停止扫描
    public void stopScan() {
        mAllBluetoothManage.stopScan();
    }

    //发送数据
    public void sendData(DeviceModule deviceModule, byte[] data) {
        mAllBluetoothManage.sendData(deviceModule, data);
    }

    //连接
    public void connect(DeviceModule deviceModule) {
        mAllBluetoothManage.connect(deviceModule);
    }

    public void tempDisconnect(DeviceModule deviceModule) {
        mAllBluetoothManage.disconnect(deviceModule);
    }

    //获取已连接的模块组
    public List<DeviceModule> getConnectedArray() {
        return mConnectedArray;
    }

    public void setOnReadListener(OnReadDataListener listener) {
        this.mDataListener = listener;
    }

    private void log(String log) {
        Log.w("AppRun" + getClass().getSimpleName(), log);
    }

    public interface UpdateList {
        void update(boolean isStart, DeviceModule deviceModule);

        void updateMessyCode(boolean isStart, DeviceModule deviceModule);
    }

    public interface OnReadDataListener {
        //获取到数据
        void readData(String mac, byte[] data);

        //数据读取中
        void reading(boolean isStart);

        //连接成功
        void connectSucceed();

        //蓝牙异常断开
        void errorDisconnect(DeviceModule deviceModule);

        //蓝牙收到数据
        void readNumber(int number);
    }
}
