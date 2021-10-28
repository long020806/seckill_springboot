package com.study.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/21 16:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object object;
    private boolean success;
    public  static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null,true);
    }
    public static RespBean success(Object object){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),object,true);
    }
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null,false);
    }
    public static RespBean error(RespBeanEnum respBeanEnum,Object object){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),object,false);
    }

}
