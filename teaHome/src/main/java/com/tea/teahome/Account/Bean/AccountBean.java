package com.tea.teahome.Account.Bean;

import android.os.Bundle;

import com.tea.teahome.Account.Utils.AccountBundleUtils;

/**
 * 账号信息类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 13:59
 */
public class AccountBean {
    /**
     * 手机号
     */
    private String phoneNum;
    /**
     * 验证码
     */
    private String code;
    /**
     * 密码
     */
    private String password;

    /**
     * 初始化类
     *
     * @param bundle 需要的Bundle信息
     */
    public AccountBean(Bundle bundle) {
        code = AccountBundleUtils.getCode(bundle);
        phoneNum = AccountBundleUtils.getPhoneNum(bundle);
        password = AccountBundleUtils.getPassword(bundle);
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}