package com.study.seckill.utils;

import java.util.UUID;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/26 14:58
 **/
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
