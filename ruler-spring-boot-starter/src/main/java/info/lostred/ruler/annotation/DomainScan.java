package info.lostred.ruler.annotation;

import java.lang.annotation.*;

/**
 * 领域模型类包扫描
 *
 * @author lostred
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainScan {
    /**
     * 包扫描路径
     *
     * @return 包扫描路径
     */
    String[] value();
}
