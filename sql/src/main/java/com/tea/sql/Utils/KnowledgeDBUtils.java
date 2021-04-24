package com.tea.sql.Utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class KnowledgeDBUtils {
    private static final String URL = "jdbc:mysql://mysql57.rdsmr3ejmq2qkwc.rds.bd.baidubce.com:3306/teahome";
    private static final String USER = "jiangyh";
    private static final String PASSWD = "jyh86350517";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2. 获得数据库连接
            conn = DriverManager.getConnection(URL, USER, PASSWD);
            Log.e("SUCCESS", "连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage() + "连接失败");
        }
        return conn;
    }
}