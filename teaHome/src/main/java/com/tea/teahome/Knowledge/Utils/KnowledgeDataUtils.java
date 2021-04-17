package com.tea.teahome.Knowledge.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.tea.teahome.Knowledge.Bean.KnowledgeBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 从data/files中获取文件
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-09 21:23
 */
public class KnowledgeDataUtils {
    /**
     * 文件后缀名
     */
    private static final String imageSuffix = ".png";
    private static final String htmlSuffix = ".html";
    /**
     * dir下的所有文件名
     */
    private final String[] filesName;
    /**
     * 文件夹目录
     */
    private final String dir;
    /**
     * 知识文件数组
     */
    private final ArrayList<KnowledgeBean> knowledgeBeanArrayList = new ArrayList<>();
    /**
     * dir下的文件
     */
    private final ArrayList<String> allHtml = new ArrayList<>();
    private final ArrayList<String> allImage = new ArrayList<>();

    /**
     * 初始化方法
     *
     * @author jiang yuhang
     * @date 2021-02-09 21:23
     **/
    public KnowledgeDataUtils(String dir) {
        this.dir = dir;

        //获取dir目录下的所有文件名,并通过后缀名分类
        filesName = getAllFileName();
        fileClassifyBySuffix();
    }

    /**
     * 分割文件名称
     * 判断image文件是否存在
     * 获得title、inf、time、url、icon信息通过KnowledgeBean传回
     *
     * @return com.tea.teahome.Knowledge.Bean.KnowledgeBean
     * @author jiang yuhang
     * @date 2021-02-16 19:57
     **/
    private KnowledgeBean splitFileName(String name) {

        KnowledgeBean knowledgeBean = new KnowledgeBean();
        String fileNameWithSuffix;//无后缀的文件名
        String[] stringSplit;//分割后的字符数组
        String imageFileName;//图片文件名称

        fileNameWithSuffix = name.substring(0, name.lastIndexOf("."));//获得无后缀的文件名

        //文件名格式为title+inf+time.html,分割后的字符串为title inf time的字符串数组。
        stringSplit = fileNameWithSuffix.split("[+]");

        //并判断image文件是否存在，若存在则icon赋值为地址，否则赋值为null
        imageFileName = fileNameWithSuffix + imageSuffix;
        if (allImage.contains(imageFileName)) {
            knowledgeBean.setIcon(getImageFromAssets(imageFileName));
        } else {
            knowledgeBean.setIcon(null);
        }

        //给Kb赋分割后的值
        knowledgeBean.setTitle(stringSplit[0]);
        knowledgeBean.setInf(stringSplit[1]);
        knowledgeBean.setTime(stringSplit[2]);
        knowledgeBean.setUrl(name);

        return knowledgeBean;
    }

    /**
     * 从assets文件夹中获取Drawable类型的图片
     *
     * @return android.graphics.drawable.Drawable
     * @author jiang yuhang
     * @date 2021-02-16 22:54
     **/
    private Drawable getImageFromAssets(String fileName) {
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable = null;

        try {
            InputStream inputStream = new FileInputStream(dir + "/" + fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
            bitmapDrawable = new BitmapDrawable(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmapDrawable;
    }

    /**
     * 遍历所有的HTML文件，读取KnowledgeBean所需要的信息。
     *
     * @return java.util.ArrayList<com.tea.teahome.Knowledge.Bean.KnowledgeBean>
     * @author jiang yuhang
     * @date 2021-02-16 19:49
     */
    public ArrayList<KnowledgeBean> getKnowledgeBeanList() {
        //遍历所有的Html文件，获取name分割后的title、inf、time
        for (String s : allHtml) {
            //返回分割后的title、inf、time、url、icon信息
            knowledgeBeanArrayList.add(splitFileName(s));
        }
        return knowledgeBeanArrayList;
    }

    /**
     * 通过后缀名对filesName中的文件进行分类。
     * Html文件放置在allHtml中，图片文件放置在allImage中。
     *
     * @author jiang yuhang
     * @date 2021-02-16 16:34
     **/
    private void fileClassifyBySuffix() {

        for (String fileName :
                filesName
        ) {
            if (fileName.substring(fileName.lastIndexOf(".")).equals(htmlSuffix)) {
                allHtml.add(fileName);
            } else if (fileName.substring(fileName.lastIndexOf(".")).equals(imageSuffix)) {
                allImage.add(fileName);
            }
        }
    }

    /**
     * 获得dir目录下的所有的文件名
     *
     * @return java.lang.String[] 文件名数组
     * @author jiang yuhang
     * @date 2021-02-16 15:30
     **/
    private String[] getAllFileName() {
        ArrayList<String> strings = new ArrayList<>();
        String[] s;
        File[] files = new File(dir).listFiles();
        try {
            //获得dir目录下的所有的文件名
            if (files != null) {
                for (File file :
                        files) {
                    //如果是文件,将文件名添加到字符数组Strings中
                    if (!file.isDirectory()) {
                        strings.add(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //如果获取失败，返回null
        }
        //将字符数组转化为字符串数组
        s = new String[strings.size()];
        int i = 0;
        for (String string :
                strings) {
            s[i++] = string;
        }
        return s;
    }
}
