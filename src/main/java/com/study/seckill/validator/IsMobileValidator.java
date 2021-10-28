package com.study.seckill.validator;

import com.study.seckill.utils.ValidateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/26 14:13
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidateUtil.isMobile(s);
        }else{
            if(StringUtils.isEmpty(s)){
                return true;
            }else{
                return ValidateUtil.isMobile(s);
            }
        }
    }
}
