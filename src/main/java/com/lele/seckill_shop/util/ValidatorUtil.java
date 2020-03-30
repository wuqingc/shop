package com.lele.seckill_shop.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("^1([38]\\d|5[0-35-9]|7[3678])\\d{8}$");

    public static boolean isMobile(String src){
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }
}
