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
    MOBILE_NOT_EXIST(500213,"手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214,"更新密码失败"),
    SESSION_ERROR(500215,"用户不存在"),
    //秒杀模块5005xx
    EMPTY_STOCK(500500,"库存不足"),
    REPEAT_ERROR(500501,"该商品没人限购一件"),
    TIME_BEFORE(500502,"秒杀还未开始"),
    TIME_AFTER(500503,"秒杀已结束"),
    REQUEST_ILLEGAL(500504,"请求非法，请重新尝试"),
    ACCESS_LIMIT_REHEAD(500505,"访问繁忙，请稍后再试"),
    //订单模块
    ORDER_NOT_EXIST(500300,"订单不存在")
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
