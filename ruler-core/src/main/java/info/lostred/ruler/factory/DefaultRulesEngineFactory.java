package info.lostred.ruler.factory;

import info.lostred.ruler.engine.RulesEngine;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认规则引擎工厂
 *
 * @author lostred
 */
public class DefaultRulesEngineFactory implements RulesEngineFactory {
    /**
     * 规则引擎缓存
     * <p>初始化后的规则引擎实现类对象会被存放在这里</p>
     */
    private final Collection<? extends RulesEngine> rulesEngines;

    public DefaultRulesEngineFactory(Collection<RulesEngine> rulesEngines) {
        this.rulesEngines = rulesEngines;
    }

    @Override
    public List<String> getAllEngineBusinessType() {
        return this.rulesEngines.stream()
                .map(RulesEngine::getBusinessType)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void reloadRules() {
        rulesEngines.forEach(RulesEngine::reloadRules);
    }

    @Override
    public List<RulesEngine> getEngine(String businessType) {
        return this.rulesEngines.stream()
                .filter(e -> e.getBusinessType().equals(businessType))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends RulesEngine> getAllEngines() {
        return this.rulesEngines;
    }
}
