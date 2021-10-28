package com.study.seckill.vo;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/21 16:51
 **/

public enum RespBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    //登录模块
    LOGIN_ERROR(500210,"用户名或密码不正确"),
    MOBILE_ERROR(500211,"手机号码格式错误"),
    BIND_ERROR(500212,"参数校验错误"),
    //秒杀模块5005xx
    EMPTY_STOCK(500500,"库存不足"),
    REPEAT_ERROR(500501,"该商品没人限购一件")
    ;
    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RespBeanEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    RespBeanEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
