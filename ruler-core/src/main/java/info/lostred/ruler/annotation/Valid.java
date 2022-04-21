package info.lostred.ruler.annotation;

import info.lostred.ruler.constant.ValidType;

import java.lang.annotation.*;

/**
 * 校验注解
 *
 * @author lostred
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Valid {
    String ruleCode();

    ValidType validType();

    Class<?> validClass();

    String[] targetMethods();
}
