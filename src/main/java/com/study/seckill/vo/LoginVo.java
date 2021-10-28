package com.study.seckill.vo;

import com.study.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/21 17:13
 **/
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    String mobile;
    @NotNull
    @Length(min=32)
    String password;

}
