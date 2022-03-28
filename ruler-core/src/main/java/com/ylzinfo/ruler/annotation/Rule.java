package com.ylzinfo.ruler.annotation;

import com.ylzinfo.ruler.constants.RulerConstants;
import com.ylzinfo.ruler.constants.ValidGrade;

import java.lang.annotation.*;

/**
 * 规则注解
 *
 * @author dengluwei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rule {
    /**
     * 规则编号
     *
     * @return 规则编号
     */
    String ruleCode();

    /**
     * 业务类型
     *
     * @return 业务类型
     */
    String businessType() default RulerConstants.DEFAULT_BUSINESS;

    /**
     * 规则校验结果等级
     *
     * @return 规则校验结果等级
     */
    ValidGrade validGrade() default ValidGrade.ILLEGAL;

    /**
     * 规则描述
     *
     * @return 规则描述
     */
    String desc();

    /**
     * 规则执行的顺序号
     *
     * @return 规则执行的顺序号
     */
    int seq() default 0;

    /**
     * 是否强制使用
     *
     * @return 是否强制使用
     */
    boolean required() default false;

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean enable() default true;

    /**
     * 规则约束的类
     *
     * @return 规则约束的类
     */
    Class<?> validClass() default Object.class;
}
