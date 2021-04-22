package com.tea.teahome.Control.util;

import androidx.annotation.NonNull;

/**
 * 将中文数字转换为阿拉伯数字
 *
 * @author jiang yuhang
 * @version 1.0
 */
public class ConvertUtil {
    public static final String allChineseNum = "零一二三四五六七八九十百";
    public static final String allArabicNum = "0123456789";

    public static Integer getArabicFromChinese(String chineseValue) {
        return ChineseNumber.getAFromC(chineseValue);
    }

    public static boolean isChineseNum(String chineseNum) {
        for (int i = 0; i < chineseNum.length(); i++) {
            if (!allChineseNum.contains(String.valueOf(chineseNum.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isArabicNum(@NonNull String arabicNum) {
        for (int i = 0; i < arabicNum.length(); i++) {
            if (!allArabicNum.contains(String.valueOf(arabicNum.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    enum ChineseNumber {
        YI('一', 1),
        ER('二', 2),
        SAN('三', 3),
        SI('四', 4),
        WU('五', 5),
        LIU('六', 6),
        QI('七', 7),
        BA('八', 8),
        JIU('九', 9),
        LING('零', 0),
        SHI('十', 10),
        BAI('百', 100),
        QIAN('千', 1000),
        WAN('万', 10000);
        private final char cValue;
        private final int aValue;

        ChineseNumber(char cValue, int aValue) {
            this.cValue = cValue;
            this.aValue = aValue;
        }

        /**
         * 将中文数字转换成阿拉伯数字
         * 0-1000
         *
         * @param cValue 中文数字
         * @return 对应的阿拉伯数字
         */
        public static Integer getAFromC(String cValue) {
            int length = cValue.length() - 1;
            int total = 0;
            while (length >= 0) {
                char chinese = cValue.charAt(length);
                int arabic = c2a(chinese);
                if (arabic >= 10 && (length - 1) >= 0) {
                    length--;
                    int arabicValue = c2a(cValue.charAt(length));
                    total = total + arabic * arabicValue;
                    length--;
                    continue;
                }
                total += arabic;
                length--;
            }
            return total;
        }

        static int c2a(char cValue) {
            for (ChineseNumber c : values()) {
                if (c.cValue == cValue) {
                    return c.aValue;
                }
            }
            return 0;
        }
    }
}