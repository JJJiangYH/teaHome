package com.tea.teahome.Account.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.tea.teahome.R;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import java.util.HashMap;

import static com.tea.teahome.Account.Utils.HttpUtils.getNetWorkBitmap;

/**
 * 存储已经登录的用户信息
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-26 14:06
 */
public class UserUtils {
    public static final String[] tempUnit = {"摄氏度", "华氏度"};
    public static final HashMap<Integer, String> regFrom = new HashMap<>();
    public static final HashMap<String, String> hashMap = new HashMap<>();
    /**
     * 账号TAG
     */
    private static final String USER = "user";

    static {
        regFrom.put(0, "邮箱");
        regFrom.put(1, "手机");
        regFrom.put(2, "其他");
        regFrom.put(3, "qq");
        regFrom.put(5, "facebook");
        regFrom.put(6, "twitter");
        regFrom.put(7, "微信");
        regFrom.put(9, "uid");
        regFrom.put(10, "google");

        hashMap.put("101", "网络异常");
        hashMap.put("102", "json 转义失败");
        hashMap.put("103", "网络错误，无法访问网络");
        hashMap.put("104", "手机时钟不对，https 证书过期");
        hashMap.put("105", "登陆需要，session 信息");
        hashMap.put("106", "同步 io 错误");
        hashMap.put("1100", "手机号码不正确");
        hashMap.put("1101", "验证码不正确");
        hashMap.put("12001", "数据解析失败");
        hashMap.put("12002", "签名不一致");
        hashMap.put("12003", "数据过期");
        hashMap.put("12004", "协议号不存在");
        hashMap.put("IS_EXISTS", "账号已存在");
    }

    public static String getTempUnit(int tempUnitId) {
        return tempUnitId == 1 ? tempUnit[0] : tempUnit[1];
    }

    public static String getRegFrom(int regFromId) {
        return regFrom.get(regFromId);
    }

    /**
     * 更改XML文件中的isLogin为b
     *
     * @param b 更改isLogin为b
     * @author jiang yuhang
     * @date 2021-02-28 14:02
     **/
    public static void setDownloadStatus(Activity activity, boolean b) {
        SharedPreferences shared = activity.getSharedPreferences(USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("isDownloaded", b);
        editor.apply();
    }

    /**
     * 更改XML文件中的isDownloaded为b
     *
     * @param b 更改isDownloaded为b
     * @author jiang yuhang
     * @date 2021-02-28 14:02
     **/
    public static void setDownloadStatus(Application activity, boolean b) {
        SharedPreferences shared = activity.getSharedPreferences(USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("isDownloaded", b);
        editor.apply();
    }

    /**
     * 获得XML文件中的isDownloaded
     *
     * @return isLogin的布尔值
     * @author jiang yuhang
     * @date 2021-02-28 14:02
     **/
    public static boolean getDownloadStatus(Activity activity) {
        SharedPreferences shared = activity.getSharedPreferences(USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shared.edit();
        return shared.getBoolean("isDownloaded", false);
    }

    /**
     * 设置账号的头像图片
     *
     * @param image 设置的srcImage图像
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    public static void setHeadIcon(Activity activity, ImageView headImage, Drawable image) {
        headImage.setBackground(activity.getDrawable(R.drawable.shape_circle_background));
        headImage.setForeground(activity.getDrawable(R.drawable.shape_circle_foreground));
        headImage.setImageDrawable(image);
    }

    /**
     * 设置账号的头像图片
     *
     * @param imageId 设置的srcImage图像
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    public static void setHeadIcon(Context context, ImageView imageView, int imageId) {
        setHeadIcon((Activity) context, imageView, context.getDrawable(imageId));
    }

    /**
     * 设置账号的头像图片
     *
     * @param url 设置的srcImage图像的网络连接
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    public static void setHeadIcon(Context context, Handler handler, String url) {
        Message message = handler.obtainMessage();
        getNetWorkBitmap(context, context.getDir("icon", Context.MODE_PRIVATE) + "icon.png", url);
        handler.sendMessage(message);
    }

    public static String getErrorCode(String code, String error) {
        if (!code.equals("")) {
            if (hashMap.get(code) != null) {
                return hashMap.get(code);
            } else {
                return error;
            }
        } else {
            switch (error) {
                case "USER_PASSWD_WRONG":
                    return "密码错误";
                case "USER_REG_PHONE_CODE_ERROR":
                    return "验证码错误";
                default:
                    return error;
            }
        }
    }

    /**
     * 修改账号昵称
     *
     * @param s 修改为的文本
     */
    public static void updateUserNickName(Context context, String s, IReNickNameCallback callback) {
        if (TuyaHomeSdk.getUserInstance().getUser() != null) {
            TuyaHomeSdk.getUserInstance().reRickName(s, callback);
        }
    }

    public static void logoutAccount(Context context, ILogoutCallback callback) {
        if (TuyaHomeSdk.getUserInstance().getUser() != null) {
            TuyaHomeSdk.getUserInstance().logout(callback);
        }
    }
}