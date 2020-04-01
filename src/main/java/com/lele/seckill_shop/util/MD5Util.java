package com.lele.seckill_shop.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String salt = "1a2b3c4d";

    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    static String inputPassToFormPass(String formPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass +
                salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String inputPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass +
                salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass,String saltDB){
        String form = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(form,saltDB);
        return dbPass;
    }
}
