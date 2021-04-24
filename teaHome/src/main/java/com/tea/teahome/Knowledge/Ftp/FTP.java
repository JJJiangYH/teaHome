package com.tea.teahome.Knowledge.Ftp;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * FTP服务工具类
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-17 14:27
 */
public class FTP {
    /**
     * UTF-8字符编码
     **/
    private static final String CHARSET_UTF8 = "UTF-8";
    /**
     * OPTS UTF8字符串常量
     **/
    private static final String OPTS_UTF8 = "OPTS UTF8";
    /**
     * 设置缓冲区大小1M
     **/
    private static final int BUFFER_SIZE = 1 << 20;
    /**
     * FTP协议里面，规定文件名编码为iso-8859-1
     **/
    private static final String serverCharset = "UTF-8";
    /**
     * 本地字符编码
     **/
    private static String localCharset = "GBK";
    /**
     * FTPClient对象
     **/
    private static FTPClient ftpClient = null;
    /**
     * FTP地址
     **/
    private final String FTP_ADDRESS;
    /**
     * FTP端口
     **/
    private final int FTP_PORT;
    /**
     * FTP用户名
     **/
    private final String FTP_USERNAME;
    /**
     * FTP密码
     **/
    private final String FTP_PASSWORD;
    /**
     * FTP基础目录
     **/
    private final String BASE_PATH;
    /**
     * 初始化登录ftp 默认false 登录成功返回true
     **/
    private Boolean b = false;

    /**
     * 初始化方法
     *
     * @author jiang yuhang
     * @date 2021-02-17 15:01
     **/
    public FTP(String FTP_ADDRESS, int FTP_PORT, String FTP_USERNAME, String FTP_PASSWORD, String BASE_PATH) {
        this.FTP_ADDRESS = FTP_ADDRESS;
        this.FTP_PORT = FTP_PORT;
        this.FTP_USERNAME = FTP_USERNAME;
        this.FTP_PASSWORD = FTP_PASSWORD;
        this.BASE_PATH = BASE_PATH;
        //登录FTP
        ftpClient = login(FTP_ADDRESS, FTP_PORT, this.FTP_USERNAME, this.FTP_PASSWORD);
    }

    /**
     * FTP服务器路径编码转换
     *
     * @param ftpPath FTP服务器路径
     * @return java.lang.String
     * @author jiang yuhang
     * @date 2021-02-17 15:02
     **/
    private static String changeEncoding(String ftpPath) {

        String directory = null;
        try {
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8, "ON"))) {
                localCharset = CHARSET_UTF8;
                System.out.println("路径编码转换成功");
            }
            directory = new String(ftpPath.getBytes(localCharset), serverCharset);
        } catch (Exception e) {
            System.out.println("路径编码转换失败" + e);
        }
        return directory;
    }

    public Boolean getB() {
        return b;
    }

    /**
     * 下载该目录下所有文件到本地
     *
     * @param ftpPath  FTP服务器上的相对路径，例如：test/123
     * @param savePath 保存文件到本地的路径，例如：D:/test
     * @author jiang yuhang
     * @date 2021-02-17 15:05
     **/
    public void downloadFiles(String ftpPath, String savePath) {
        // 登录
        if (ftpClient != null) {
            try {
                String path = changeEncoding(BASE_PATH + ftpPath);
                // isExist
                if (isExist(ftpPath, !ftpClient.changeWorkingDirectory(path), "该目录不存在"))
                    return;
                ftpClient.enterLocalPassiveMode();  // 设置被动模式，开通一个端口来传输数据
                String[] fs = ftpClient.listNames();// 获取所有文件名
                // 判断该目录下是否有文件
                if (isExist(ftpPath, fs == null || fs.length == 0, "该目录下没有文件")) {
                    return;
                }
                //计算下载时间
                long startTime = System.currentTimeMillis();
                //建立线程池
                ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
                //遍历每一个文件
                for (String ff : fs) {
                    //进行下载操作
                    fixedThreadPool.execute(() -> {
                        try {
                            FTPClient ftpClient = login(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD);
                            String ftpName = new String(ff.getBytes(serverCharset), localCharset);
                            File file = new File(savePath + '/' + ftpName);
                            OutputStream os = new FileOutputStream(file);
                            //从FTP获取文件
                            ftpClient.retrieveFile(ff, os);
                            os.close();
                            ftpClient.logout();
                            Log.e("error", "下载成功");
                        } catch (Exception e) {
                            Log.e(e.getMessage(), e.getMessage() + e);
                        }
                    });
                }//for
                fixedThreadPool.shutdown();//关闭线程池

                //判断线程池内的线程是否完成，若完成则继续执行下方代码，否则一直等待
                while (true) {
                    if (fixedThreadPool.isTerminated()) {
                        break;
                    }
                }
                ftpClient.logout();

                //输出下载时间
                long endTime = System.currentTimeMillis(); // 获取完成时间
                System.out.println("代码运行时间： " + (endTime - startTime) + "ms");
            } catch (IOException e) {
                System.out.println("下载文件失败" + e);
            }
        }
    }

    /**
     * 使用b判断，如果为真，返回真，如果为假，返回假
     *
     * @param ftpPath, b, s
     * @return boolean
     * @author jiang yuhang
     * @date 2021-02-19 16:53
     **/
    private boolean isExist(String ftpPath, boolean b, String s) {
        if (b) {
            System.out.println(BASE_PATH + ftpPath + s);
            return true;
        }
        return false;
    }

    /**
     * 连接FTP服务器
     *
     * @param address  地址，如：127.0.0.1
     * @param port     端口，如：21
     * @param username 用户名，如：root
     * @param password 密码，如：root
     * @return java.lang.Boolean
     * @author jiang yuhang
     * @date 2021-02-17 15:08
     **/
    private FTPClient login(String address, int port, String username, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setBufferSize(BUFFER_SIZE);
            ftpClient.connect(address, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                closeConnect();
            } else {
                b = true;
            }
        } catch (Exception e) {
        }
        return ftpClient;
    }

    /**
     * 关闭FTP连接
     *
     * @author jiang yuhang
     * @date 2021-02-17 15:09
     **/
    public void closeConnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                System.out.println("关闭FTP连接失败" + e);
            }
        }
    }
}