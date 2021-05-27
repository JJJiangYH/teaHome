package com.hc.bluetoothlibrary.bleBluetooth;

import android.annotation.SuppressLint;

import java.nio.charset.StandardCharsets;


public class ParseLeAdvData {
    //BLE 广播包数据类型
    /**
     * < Partial list of 16 bit service UUIDs.
     */
    public static final short BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE = 0x02;
    /**
     * < Complete list of 16 bit service UUIDs.
     */
    public static final short BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE = 0x03;
    /**
     * < Complete local device name.
     */
    public static final short BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME = 0x09;
    /**
     * < Public Target Address.
     */
    public static final short BLE_GAP_AD_TYPE_PUBLIC_TARGET_ADDRESS = 0x17;
    /**
     * < Random Target Address.
     */
    public static final short BLE_GAP_AD_TYPE_RANDOM_TARGET_ADDRESS = 0x18;

    /**
     * 解析广播数据
     *
     * @param type     类型：查看上面常量
     * @param adv_data 解析包数据
     * @return 指定类型的数据
     */
    public static byte[] adv_report_parse(short type, byte[] adv_data) {

        int index = 0;

        int length;

        byte[] data;

        byte field_type;

        byte field_length;

        length = adv_data.length;

        while (index < length) {

            try {

                field_length = adv_data[index];

                field_type = adv_data[index + 1];

            } catch (Exception e) {


                return null;

            }

            if (field_type == (byte) type) {

                data = new byte[field_length - 1];

                byte i;

                for (i = 0; i < field_length - 1; i++) {

                    data[i] = adv_data[index + 2 + i];

                }

                return data;

            }

            index += field_length + 1;

            if (index >= adv_data.length) {

                return null;

            }

        }

        return null;

    }

    /**
     * 获得本地名称
     *
     * @param adv_data 广播数据
     */
    public static String getLocalName(byte[] adv_data) {
        byte[] data = adv_report_parse(BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME, adv_data);
        if (data != null) {
            return byteArrayToGbkString(data, data.length);
        }
        return null;
    }

    public static String getShort16(byte[] adv_data) {
        byte[] data = adv_report_parse(BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE, adv_data);
        StringBuilder dataStr = new StringBuilder();
        if (data != null) {
            for (int i = 0; i < data.length / 2; i++) {
                byte[] by = {data[i * 2 + 1], data[i * 2]};
                dataStr.append(bytesToHexString(by));
            }
        } else {
            data = adv_report_parse(BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE, adv_data);
            if (data != null) {
                for (int i = 0; i < data.length / 2; i++) {
                    byte[] by = {data[i * 2 + 1], data[i * 2]};
                    dataStr.append(bytesToHexString(by));
                }
            }
        }


        if (dataStr.toString().equals("")) {
            return null;
        } else {
            return dataStr.toString();
        }
    }

    /**
     * byte to Hex String
     *
     * @param barray byte array
     * @return hex string
     */
    private static String bytesToHexString(byte[] barray) {
        if (barray == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String stemp;
        for (byte b : barray) {
            stemp = Integer.toHexString(0xFF & b);
            if (stemp.length() < 2) {
                sb.append(0);
            }
            sb.append(stemp.toUpperCase());
            // sb.append("  ");
        }
        return "0x" + sb.toString();
    }

    @SuppressLint("NewApi")
    private static String byteArrayToGbkString(byte[] inarray, int len) {

        String gbkstr = "";
        int idx;
        if (inarray != null) {
            for (idx = 0; idx < len; idx++) {
                if (inarray[idx] == 0x00) {
                    break;
                }
            }
            gbkstr = new String(inarray, 0, idx, StandardCharsets.UTF_8);
        }
        return gbkstr;
    }
}
