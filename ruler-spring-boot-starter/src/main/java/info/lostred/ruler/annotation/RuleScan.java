package info.lostred.ruler.annotation;

import java.lang.annotation.*;

/**
 * 规则类包扫描注解
 *
 * @author lostred
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
