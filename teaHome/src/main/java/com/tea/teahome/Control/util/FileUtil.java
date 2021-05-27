package com.tea.teahome.Control.util;

import java.io.File;

/**
 * Created by fujiayi on 2017/5/19.
 */

public class FileUtil {
    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return !file.mkdirs();
        } else {
            return false;
        }
    }
}
