package com.tea.teahome.Knowledge.Utils;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 16:08
 */
public class StringUtils {
    /**
     * 去掉字符串中的+
     *
     * @param string 要处理的字符串
     * @return 去除+号的字符串
     * @author jiang yuhang
     * @date 2021-02-23 0:30
     **/
    public static String removeAddSign(String string) {
        StringBuilder sb = new StringBuilder();
        char[] chars = string.toCharArray();

        for (char c :
                chars) {
            if (c != '+') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}