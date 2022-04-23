package info.lostred.ruler.factory;

import info.lostred.ruler.engine.RulesEngine;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认规则引擎工厂
 *
 * @author lostred
 */
public class DefaultRulesEngineFactory implements RulesEngineFactory {
    private final Map<String, ? extends RulesEngine> rulesEngines;

    public DefaultRulesEngineFactory(Collection<RulesEngine> rulesEngines) {
        this.rulesEngines = rulesEngines.stream()
                .collect(Collectors.toMap(RulesEngine::getBusinessType, e -> e));
    }

    @Override
    public List<String> getAllEngineBusinessType() {
        return this.rulesEngines.values().stream()
                .map(RulesEngine::getBusinessType)
                .collect(Collectors.toList());
    }

    @Override
    public RulesEngine getEngine(String businessType) {
        return this.rulesEngines.get(businessType);
    }
}
