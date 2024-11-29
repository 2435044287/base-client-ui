package pers.yuews.bcu.core.utils;

/**
 * @author yuews
 * @create 2024/6/3 16:34
 * @describe
 */
public class StringUtils {

    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

}
