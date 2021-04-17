package com.tea.teahome.Account.Utils;

import android.os.Bundle;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 14:32
 */
public class AccountBundleUtils {
    public static String getCountryCode(Bundle bundle) {
        return bundle.getString("countryCode");
    }

    public static void putTag(Bundle bundle, String tag) {
        bundle.putString("activity", tag);
    }

    public static String getTag(Bundle bundle) {
        return bundle.getString("activity");
    }

    public static String getPhoneNum(Bundle bundle) {
        return bundle.getString("phoneNum");
    }

    public static String getPassword(Bundle bundle) {
        return bundle.getString("password");
    }

    public static boolean getCodeMode(Bundle bundle) {
        return bundle.getBoolean("isCodeMode");
    }

    public static String getCode(Bundle bundle) {
        return bundle.getString("code");
    }

    public static void putCountryCode(Bundle bundle, String countryCode) {
        bundle.putString("countryCode", countryCode);
    }

    public static void putPhoneNum(Bundle bundle, String phoneNum) {
        bundle.putString("phoneNum", phoneNum);
    }

    public static void putPassword(Bundle bundle, String password) {
        bundle.putString("password", password);
    }

    public static void putCodeMode(Bundle bundle, boolean isCodeMode) {
        bundle.putBoolean("isCodeMode", isCodeMode);
    }

    public static void putCode(Bundle bundle, String code) {
        bundle.putString("code", code);
    }

}