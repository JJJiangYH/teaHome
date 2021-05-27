package com.tea.teahome.User.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-03-02 15:52
 */
public class BitmapUtils {
    /**
     * 将输入的头像文件裁剪为正方形且文件大小不大于1MB的图片
     *
     * @return 不大于1MB的正方形图像文件
     */
    public static Bitmap getHeadBitmap(InputStream is) throws Exception {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        byte[] bytes = getBytes(is);
        InputStream is1 = new ByteArrayInputStream(bytes);
        InputStream is2 = new ByteArrayInputStream(bytes);

        //不加载图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is1, null, options);
        //计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options);
        //加载图片
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeStream(is2, null, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int headSize = Math.min(width, height);

        bitmap = Bitmap.createBitmap(bitmap, (width - headSize) / 2,
                (height - headSize) / 2, headSize, headSize);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > 300 || width > 300) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= 300
                    && (halfWidth / inSampleSize) >= 300) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    protected static byte[] getBytes(InputStream source) throws Exception {
        ByteArrayOutputStream baos;
        baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = source.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos.toByteArray();
    }
}