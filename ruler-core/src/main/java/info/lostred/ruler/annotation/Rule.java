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
     * 参数表达式
     *
     * @return 参数表达式
     */
    String parameterExp();

    /**
     * 条件表达式
     *
     * @return 条件表达式
     */
    String conditionExp();

    /**
     * 断定表达式
     *
     * @return 断定表达式
     */
    String predicateExp();
}
