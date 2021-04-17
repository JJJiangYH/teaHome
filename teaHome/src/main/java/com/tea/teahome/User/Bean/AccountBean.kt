package com.tea.teahome.User.Bean

import android.os.Bundle
import com.tea.teahome.User.Utils.AccountBundleUtils

/**
 * 账号信息类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 13:59
 */
class AccountBean(bundle: Bundle?) {
    /**
     * 手机号
     */
    var phoneNum: String? = AccountBundleUtils.getPhoneNum(bundle)

    /**
     * 验证码
     */
    var code: String? = AccountBundleUtils.getCode(bundle)

    /**
     * 密码
     */
    var password: String? = AccountBundleUtils.getPassword(bundle)
}