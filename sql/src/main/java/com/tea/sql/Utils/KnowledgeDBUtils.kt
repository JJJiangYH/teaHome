package com.tea.sql.Utils

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object KnowledgeDBUtils {
    const val URL = "jdbc:mysql://mysql57.rdsmr3ejmq2qkwc.rds.bd.baidubce.com:3306/teahome"
    const val USER = "jiangyh"
    const val PASSWD = "jyh86350517"
    var connection: Connection? = null
        private set

    init {
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver")
            //2. 获得数据库连接
            connection = DriverManager.getConnection(URL, USER, PASSWD)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}