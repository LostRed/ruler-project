package info.lostred.ruler.factory;

import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.exception.RulesEnginesException;

import java.util.Collection;
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
    public RulesEngine getEngine(String businessType) {
        RulesEngine rulesEngine = this.rulesEngines.get(businessType);
        if (rulesEngine == null) {
            throw new RulesEnginesException("There is not available rules engine for '" + businessType + "' business type.", businessType, null);
        }
        return rulesEngine;
    }
}
