package com.tea.teahome.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.tea.teahome.Widget.StatusBar;

/**
 * 设置View的内容的方法类
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-12 19:26
 */
public class ViewUtil {
    /**
     * 设置textViewID所对应的TextView的文本内容
     *
     * @param activity   所在的activity
     * @param string     文本内容
     * @param textViewID TextViewID
     * @author jiang yuhang
     * @date 2021-02-19 19:48
     **/
    public static void setText(Activity activity, String string, int textViewID) {
        TextView textView = activity.findViewById(textViewID);
        textView.setText(string);
    }

    /**
     * 设置textViewID所对应的TextView的文本内容
     *
     * @param view       所在的View
     * @param string     文本内容
     * @param textViewID TextViewID
     * @author jiang yuhang
     * @date 2021-02-19 19:48
     **/
    public static void setText(View view, String string, int textViewID) {
        TextView textView = view.findViewById(textViewID);
        textView.setText(string);
    }

    /**
     * 设置imageViewID所对应的ImageView的图像信息
     *
     * @param drawable    要设置的图片
     * @param imageViewID 要设置的IV的ID
     * @author jiang yuhang
     * @date 2021-02-16 22:07
     **/
    public static void setImage(Activity activity, Drawable drawable, int imageViewID) {

        ImageView imageView = activity.findViewById(imageViewID);
        imageView.setImageDrawable(drawable);
    }

    /**
     * 设置imageViewID所对应的ImageView的图像信息
     *
     * @param drawable    要设置的图片
     * @param imageViewID 要设置的IV的ID
     * @author jiang yuhang
     * @date 2021-02-16 22:07
     **/
    public static void setImage(View view, Drawable drawable, int imageViewID) {

        ImageView imageView = view.findViewById(imageViewID);
        imageView.setImageDrawable(drawable);
    }

    /**
     * 设置TextViewID对应的文字
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setTitle(Activity activity, String string, int textViewID) {
        setText(activity, string, textViewID);
    }

    /**
     * 设置TextViewID对应的文字
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setTitle(View view, String string, int textViewID) {
        setText(view, string, textViewID);
    }

    /**
     * 设置TextViewID对应的文字
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setTime(View view, String string, int textViewID) {
        setText(view, string, textViewID);
    }

    /**
     * 设置TextViewID对应的文字
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setTime(Activity activity, String string, int textViewID) {
        setText(activity, string, textViewID);
    }

    /**
     * 设置TextViewID对应的文字
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setInf(Activity activity, String string, int textViewID) {
        setText(activity, string, textViewID);
    }

    /**
     * 设置TextViewID对应的文字
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setInf(View view, String string, int textViewID) {
        setText(view, string, textViewID);
    }

    /**
     * 设置imageViewID所对应的图像内容
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setIcon(View view, Drawable drawable, int textViewID) {
        setImage(view, drawable, textViewID);
    }

    /**
     * 设置WebView网页
     *
     * @param activity  设置的视窗的根
     * @param mFilePath 文件地址
     * @param webViewID 视窗ID
     */
    public static void setWeb(Activity activity, String mFilePath, int webViewID) {
        WebView webView = activity.findViewById(webViewID);
        webView.loadUrl(mFilePath);
        webView.setWebViewClient(new WebViewClient());
    }

    /**
     * 设置imageViewID所对应的图像内容
     *
     * @author jiang yuhang
     * @date 2021-02-21 20:00
     **/
    public static void setIcon(Activity activity, Drawable drawable, int textViewID) {
        setImage(activity, drawable, textViewID);
    }

    /**
     * 设置WebView网页
     *
     * @param view      设置的视窗的根
     * @param mFilePath 文件地址
     * @param webViewID 视窗ID
     */
    public static void setWeb(View view, String mFilePath, int webViewID) {
        WebView webView = view.findViewById(webViewID);
        webView.loadUrl(mFilePath);
        webView.setWebViewClient(new WebViewClient());
    }

    /**
     * 关闭Activity
     *
     * @param activity 要关闭的Activity
     */
    public static void closeActivity(Activity activity) {
        activity.finish();
    }

    /**
     * 获取view所在的activity
     *
     * @param view 窗口
     * @return Activity view所在的activity
     */
    public static Activity getActivityFromView(View view) {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

    /**
     * @param view 要关闭的activity中的视图
     * @author jiang yuhang
     * 关闭按钮响应函数
     * @date 2021-02-22 21:08
     **/
    public static void closeActivity(View view) {
        closeActivity(getActivityFromView(view));
    }

    /**
     * 给Id对应的组件添加closeListener
     *
     * @param Id 要添加监听的组件Id
     */
    public static void addCloseButtonListener(Activity activity, int Id) {
        ImageView imageView = activity.findViewById(Id);
        imageView.setOnClickListener(ViewUtil::closeActivity);
    }

    /**
     * 添加状态栏的遮挡
     *
     * @param context  上下文
     * @param color    状态栏遮挡的颜色 #rrggbb
     * @param layoutId 根布局
     * @author jiang yuhang
     * @date 2021-02-25 17:37
     **/
    public static void addStatusBar(Context context, int layoutId, int color) {
        ViewGroup v = ((Activity) context).findViewById(layoutId);
        v.addView(new StatusBar(context, color), 0);
    }

    /**
     * @return maxLength属性
     * @author jiang yuhang
     * 返回EditText对象的Maxlength属性
     * @date 2021-02-22 22:13
     **/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static int getMaxLength(EditText editText) {
        return ((InputFilter.LengthFilter) editText.getFilters()[0]).getMax();
    }
}
