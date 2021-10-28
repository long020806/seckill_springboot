package com.study.seckill.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/26 10:31
 **/
@Slf4j
public class ValidateUtil {
    private static final Pattern mobile_pattern = Pattern.compile("1[3-9]\\d{9}");

    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)) return false;
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
