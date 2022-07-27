package info.lostred.ruler.constant;

/**
 * 严重等级
 *
 * @author lostred
 */
public enum Grade {
    /**
     * 合格
     * <p>
     * 对于校验结果Result来说，表示校验数据对象中没有违反对应规则引擎中的任何一条规则
     * </p>
     *
     * @see info.lostred.ruler.domain.Result
     */
    QUALIFIED,
    /**
     * 可疑
     * <p>
     * 对于校验结果Result来说，表示校验数据对象中违反了对应规则引擎中警告等级为可疑的规则，但没有违反警告等级为非法的规则
     * </p>
     *
     * @see info.lostred.ruler.domain.Result
     */
    SUSPECTED,
    /**
     * 非法
     * <p>
     * 对于校验结果Result来说，表示校验数据对象中违反了对应规则引擎中任意一条警告等级为非法的规则
     * </p>
     *
     * @see info.lostred.ruler.domain.Result
     */
    ILLEGAL
}
