package com.tea.teahome.User.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 位图工具类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-03-02 11:34
 */
public class DrawableUtils {
    /**
     * drawable转为file
     *
     * @param drawableId drawable的ID
     * @return file
     */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File drawableToFile(Context context, int drawableId) {
        String path = context.getDir("icon", Context.MODE_PRIVATE).toString() + "/icon.png";
        File file = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            @SuppressLint("UseCompatLoadingForDrawables")
            BitmapDrawable b = (BitmapDrawable) context.getResources().getDrawable(drawableId);
            b.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * drawable转为file
     *
     * @return file
     */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File bitmapToFile(Context context, Bitmap bitmap) {
        String path = context.getCacheDir().toString() + "/icon.png";
        File file = new File(path);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}