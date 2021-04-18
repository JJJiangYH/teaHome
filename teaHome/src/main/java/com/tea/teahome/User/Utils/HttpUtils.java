package com.tea.teahome.User.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-03-02 00:43
 */
public class HttpUtils {
    /**
     * 账号TAG
     */
    private static final String USER = "user";
    /**
     * 上一个头像
     */
    private static final String LAST_HEAD_ICON = "lastHeadIconURL";
    /**
     * 不存在该信息
     */
    private static final String NOT_EXIST_STRING = "No_STRING";


    /**
     * 获取网络图片
     *
     * @param urlString 如：http://f.hiphotos.baidu.com/image/w%3D2048/sign=3
     *                  b06d28fc91349547e1eef6462769358
     *                  /d000baa1cd11728b22c9e62ccafcc3cec2fd2cd3.jpg
     * @date 2014.05.10
     */
    public static void getNetWorkBitmap(Context context, String downPath, String urlString) {
        String lastUrl = getLastUrl(context);

        if (!lastUrl.equals(urlString)) {
            setLastUrl(context, urlString);

            URL imgUrl;
            Bitmap bitmap;
            try {
                imgUrl = new URL(urlString);
                // 使用HttpURLConnection打开连接
                HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
                urlConn.setDoInput(true);
                urlConn.connect();
                // 将得到的数据转化成InputStream
                InputStream is = urlConn.getInputStream();
                // 将InputStream转换成Bitmap
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                saveBitmapToFile(downPath, bitmap);
            } catch (MalformedURLException e) {
                System.out.println("[getNetWorkBitmap->]MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("[getNetWorkBitmap->]IOException");
                e.printStackTrace();
            }
        }
    }

    public static void saveBitmapToFile(String downPath, Bitmap bitmap) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(downPath);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
    }

    public static void setLastUrl(Context context, String urlString) {
        SharedPreferences shared = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shared.edit();
        editor.putString(LAST_HEAD_ICON, urlString);
        editor.apply();
    }

    public static String getLastUrl(Context context) {
        SharedPreferences shared = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        return shared.getString(LAST_HEAD_ICON, NOT_EXIST_STRING);
    }
}