package info.lostred.ruler.constant;

/**
 * 校验类型
 *
 * @author lostred
 */
public enum ValidType {
    /**
     * 该字段不能为空
     */
    REQUIRED,
    /**
     * 该字段必须是字典值
     */
    DICT,
    /**
     * 该数值字段必须在范围内
     */
    NUMBER_SCOPE,
    /**
     * 该日期时间字段必须在范围内
     */
    DATETIME_SCOPE
}
