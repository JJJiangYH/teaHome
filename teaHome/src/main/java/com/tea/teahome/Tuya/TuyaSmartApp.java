package com.tea.teahome.Tuya;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tea.teahome.Account.Activity.LoginAccountActivity;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

import static com.tea.teahome.Account.Utils.HttpUtils.getNetWorkBitmap;
import static com.tea.teahome.Account.Utils.UserUtils.setDownloadStatus;

/**
 * 继承Application，进行涂鸦智能SDK初始化
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-07 20:48
 */
public class TuyaSmartApp extends Application {
    /**
     * 活动
     */
    private Application application;

    /**
     * TuyaSmart SDK初始化
     *
     * @author jiang yuhang
     * @date 2021-02-16 17:56
     **/
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // SDK 初始化
        TuyaHomeSdk.init(this);
        //检测账号是否过期
        TuyaHomeSdk.setOnNeedLoginListener(
                context ->
                {
                    Intent intent = new Intent(application, LoginAccountActivity.class);
                    startActivity(intent);
                });
        TuyaHomeSdk.getUserInstance().updateUserInfo(new IResultCallback() {
            @Override
            public void onError(String code, String error) {
            }

            @Override
            public void onSuccess() {
                User user = TuyaHomeSdk.getUserInstance().getUser();
                new Thread(
                        () ->
                        {
                            setDownloadStatus(application, false);
                            if (user != null) {
                                getNetWorkBitmap(application, application.getDir("icon", Context.MODE_PRIVATE) + "icon.png",
                                        user.getHeadPic());
                            }
                            setDownloadStatus(application, true);
                        }).start();
            }
        });
    }
}
