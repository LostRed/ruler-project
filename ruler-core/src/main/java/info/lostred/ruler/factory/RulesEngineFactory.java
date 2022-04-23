package info.lostred.ruler.factory;

import info.lostred.ruler.engine.AbstractRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
     * 从引擎单例池中获取规则引擎
     *
     * @param businessType 业务类型
     * @return 规则引擎
     */
    RulesEngine getEngine(String businessType);

    /**
     * 获取工厂的建造者对象
     *
     * @param ruleFactory      规则工厂
     * @param businessType     业务类型
     * @param beanResolver     Bean解析器
     * @param parser           表达式解析器
     * @param rulesEngineClass 规则引擎类的类对象
     * @param <T>              规则引擎类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    static <T extends AbstractRulesEngine> Builder<T> builder(RuleFactory ruleFactory, String businessType,
                                                              BeanResolver beanResolver, ExpressionParser parser,
                                                              Class<T> rulesEngineClass) {
        return new Builder<>(ruleFactory, businessType, beanResolver, parser, rulesEngineClass);
    }

    /**
     * 规则引擎建造者
     *
     * @param <T> 规则引擎类型
     */
    class Builder<T extends AbstractRulesEngine> {
        private final RuleFactory ruleFactory;
        private final String businessType;
        private final BeanResolver beanResolver;
        private final ExpressionParser parser;
        private final Class<T> rulesEngineClass;

        public Builder(RuleFactory ruleFactory, String businessType,
                       BeanResolver beanResolver, ExpressionParser parser,
                       Class<T> rulesEngineClass) {
            this.ruleFactory = ruleFactory;
            this.businessType = businessType;
            this.beanResolver = beanResolver;
            this.parser = parser;
            this.rulesEngineClass = rulesEngineClass;
        }

        /**
         * 实例化规则引擎
         *
         * @return 规则引擎实例
         */
        public T build() {
            try {
                Constructor<T> constructor = rulesEngineClass.getDeclaredConstructor(
                        RuleFactory.class, String.class, BeanResolver.class, ExpressionParser.class);
                return constructor.newInstance(ruleFactory, businessType, beanResolver, parser);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
