package info.lostred.ruler.annotation;

import info.lostred.ruler.constant.Grade;

import java.lang.annotation.*;

/**
 * 规则注解
 *
 * @author lostred
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
    String ruleCode() default "";

    /**
     * 业务类型
     *
     * @return 业务类型
     */
    String businessType();

    /**
     * 规则的严重等级
     *
     * @return 规则的严重等级
     */
    Grade grade() default Grade.ILLEGAL;

    /**
     * 规则描述
     *
     * @return 规则描述
     */
    String description();

    /**
     * 规则执行的顺序号
     *
     * @return 规则执行的顺序号
     */
    int order() default 0;

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
     * 参数表达式
     * <p>定义入参的取值字段</p>
     *
     * @return 参数表达式
     */
    String parameterExp() default "";

    /**
     * 条件表达式
     * <p>定义规则生效的条件，期望返回一个布尔值，值为true时规则才会生效</p>
     *
     * @return 条件表达式
     */
    String conditionExp() default "";

    /**
     * 断定表达式
     * <p>定义规则的运行逻辑，期望返回一个布尔值，值为true时表示参数不符合要求</p>
     *
     * @return 断定表达式
     */
    String predicateExp() default "";
}
