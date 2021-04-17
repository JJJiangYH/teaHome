package com.tea.teahome.Knowledge.Utils;

import com.tea.teahome.Knowledge.Ftp.FTP;

/**
 * 通过FTP获取知识数据
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-17 15:45
 */

public class KnowledgeFTPUtils {
    /**
     * FTP
     */
    private final FTP ftp;
    /**
     * FTP登录是否成功
     */
    private final Boolean FTPBool;
    /**
     * 文件地址
     */
    private final String privatePath;

    /**
     * 初始化变量，获取FTp中的文件
     *
     * @author jiang yuhang
     * @date 2021-02-17 15:52
     **/
    public KnowledgeFTPUtils(String privatePath) {
        this.privatePath = privatePath;

        String FTP_ADDRESS = "106.13.54.66";
        int FTP_PORT = 21;
        String FTP_USERNAME = "jiangyuhang";
        String FTP_PASSWORD = "Jyh86350517";
        String BASE_PATH = "";

        this.ftp = new FTP(FTP_ADDRESS, FTP_PORT, FTP_USERNAME
                , FTP_PASSWORD, BASE_PATH);
        //获取FTP登录是否成功
        this.FTPBool = ftp.getB();
    }

    /**
     * 如果FTP登陆成功，下载全部的文件到PrivatePath中
     *
     * @author jiang yuhang
     * @date 2021-02-17 22:26
     **/
    public void downloadFilesFromFtp() {
        ftp.downloadFiles("/", privatePath);
    }

    public Boolean isConnected() {
        return FTPBool;
    }
}