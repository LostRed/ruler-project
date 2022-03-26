package com.ylzinfo.ruler.annotation;

import java.lang.annotation.*;

/**
 * 规则包扫描注解
 *
 * @author dengluwei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RuleScan {
    /**
     * 包扫描路径
     *
     * @return 包扫描路径
     */
    String[] value();
}
