package info.lostred.ruler.constant;

/**
 * 引擎类型
 *
 * @author lostred
 */
public enum EngineType {
    /**
     * 规则引擎将执行完所有的规则
     */
    COMPLETE,
    /**
     * 规则引擎在非法等级规则触发后将结束执行
     */
    INCOMPLETE
}
