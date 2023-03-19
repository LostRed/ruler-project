package info.lostred.ruler.factory;

import info.lostred.ruler.engine.RulesEngine;

import java.util.Collection;
import java.util.List;

/**
 * 规则引擎工厂
 *
 * @author lostred
 */
public interface RulesEngineFactory {
    /**
     * 获取所有引擎的业务类型
     *
     * @return 业务类型集合
     */
    List<String> getAllEngineBusinessType();

    /**
     * 所有引擎从规则工厂中重新加载规则
     */
    void reloadRules();

    /**
     * 从引擎单例池中获取规则引擎
     *
     * @param businessType 业务类型
     * @return 规则引擎
     */
    RulesEngine getEngine(String businessType);

    /**
     * 获取所有规则引擎
     *
     * @return 规则引擎集合
     */
    Collection<? extends RulesEngine> getAllEngines();
}
