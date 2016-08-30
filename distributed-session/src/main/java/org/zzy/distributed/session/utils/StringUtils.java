package org.zzy.distributed.session.utils;

/**
 * Created by zhaoyu on 16-8-25.
 */
public abstract class StringUtils {

    private StringUtils() {}

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return true;
        }
    }
}
