package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.lostred.ruler.constant.RulerConstants.SP_EL_INDEX;
import static info.lostred.ruler.constant.RulerConstants.SP_EL_PREFIX;

public class RulesEngine {

    private final String businessType;
    protected ExpressionParser parser;
    private EvaluationContext context;
    private final List<AbstractRule> rules = new CopyOnWriteArrayList<>();

    public RulesEngine(String businessType, ExpressionParser parser, Collection<AbstractRule> rules) {
        this.businessType = businessType;
        this.parser = parser;
        this.rules.addAll(rules);
    }

    public Result execute(Object object) {
        context = new StandardEvaluationContext(object);
        Result result = Result.of();
        for (AbstractRule rule : rules) {
            String parameterExp = rule.getRuleDefinition().getParameterExp();
            if (parameterExp.contains(SP_EL_INDEX)) {
                String collectionExp = parameterExp.substring(0, parameterExp.indexOf(SP_EL_INDEX));
                Collection<?> collection = parser.parseExpression(SP_EL_PREFIX + collectionExp).getValue(context, Collection.class);
                this.executeForArray(collection, rule, result);
            } else {
                this.executeForOne(object, rule, result);
            }
        }
        result.updateGrade();
        result.statistic();
        return result;
    }

    private void executeForArray(Collection<?> collection, AbstractRule rule, Result result) {
        if (collection != null) {
            for (int i = 0; i < collection.size(); i++) {
                context.setVariable("i", i);
                this.executeForOne(collection.toArray()[i], rule, result);
            }
        }
    }

    public void executeForOne(Object object, AbstractRule rule, Result result) {
        if (rule.isSupported(context, parser, object)) {
            if (rule.judge(context, parser, object)) {
                Map<String, Object> map = rule.collectMappings(context, parser, object);
                Report report = Report.of(rule.getRuleDefinition()).putError(map);
                result.addReport(report);
            }
        }
    }

    public void setVariable(String name, Object value) {
        context.setVariable(name, value);
    }
    public String getBusinessType() {
        return businessType;
    }
}
