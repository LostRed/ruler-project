package info.lostred.ruler.factory;

import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.engine.RulesEngine;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认规则引擎工厂
 *
 * @author lostred
 */
public class DefaultRulesEngineFactory implements RulesEngineFactory {
    private final Map<String, ? extends RulesEngine<?>> rulesEngines;

    public DefaultRulesEngineFactory(Collection<RulesEngine<?>> rulesEngines) {
        this.rulesEngines = rulesEngines.stream()
                .collect(Collectors.toMap(RulesEngine::getBusinessType, e -> e));
    }

    @Override
    public <E> RulesEngine<E> getEngine(Object object, Class<E> validClass) {
        return this.getEngine(RulerConstants.DEFAULT_BUSINESS_TYPE, object, validClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> RulesEngine<E> getEngine(String businessType, Object object, Class<E> validClass) {
        RulesEngine<E> rulesEngine = (RulesEngine<E>) this.rulesEngines.get(businessType);
        if (rulesEngine == null) {
            throw new RuntimeException("There is not available rules engine for '" + businessType + "' business type.");
        }
        return rulesEngine;
    }
}
