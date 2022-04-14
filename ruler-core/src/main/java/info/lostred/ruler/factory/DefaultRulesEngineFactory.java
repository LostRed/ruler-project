package info.lostred.ruler.factory;

import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.RulesEngine;

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
    public <E> RulesEngine<E> dispatch(Object validRootNode, Class<E> validClass) {
        return this.dispatch(RulerConstants.DEFAULT_BUSINESS_TYPE, validRootNode, validClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> RulesEngine<E> dispatch(String businessType, Object validRootNode, Class<E> validClass) {
        RulesEngine<E> rulesEngine = (RulesEngine<E>) this.rulesEngines.get(businessType);
        if (rulesEngine == null) {
            throw new RuntimeException("Cannot dispatch this business type, because has not available rules engine.");
        }
        return rulesEngine;
    }
}
