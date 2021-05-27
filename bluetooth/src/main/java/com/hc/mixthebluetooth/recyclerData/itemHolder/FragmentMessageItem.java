package com.hc.mixthebluetooth.recyclerData.itemHolder;

import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.activity.tool.Analysis;

public class FragmentMessageItem {

    private final byte[] byteData;
    private final String time;
    private final DeviceModule module;
    private final boolean isHex;

    public FragmentMessageItem(boolean isHex, byte[] data, String time, DeviceModule module) {
        this.byteData = data;
        this.module = module;
        this.time = time;
        this.isHex = isHex;
    }

    public DeviceModule getModule() {
        return module;
    }

    public String getData() {
        return Analysis.getByteToString(byteData, isHex);
    }

    public byte[] getByteData() {
        return byteData;
    }

    public String getTime() {
        return time;
    }
}
