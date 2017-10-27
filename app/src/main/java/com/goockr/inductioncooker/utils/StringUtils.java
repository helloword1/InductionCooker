package com.goockr.inductioncooker.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LJN on 2017/10/18.
 */

public class StringUtils {
    private static Pattern P = Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|14[0-9])\\d{8}$");
    /**
     * 判断是不是一个合法的手机号码
     *
     * @return
     */
    public static boolean isPhone(String mobiles) {
        Matcher m = P.matcher(mobiles);
        return m.find();
    }
}
