/**
 * <h2>抽象规则{@link info.lostred.ruler.core.AbstractRule}的实现类包</h2>
 * <p>工程默认提供四个实现类：{@link info.lostred.ruler.rule.RequiredFieldRule}、
 * {@link info.lostred.ruler.rule.DictFieldRule}、{@link info.lostred.ruler.rule.NumberScopeFieldRule}、
 * {@link info.lostred.ruler.rule.DateTimeScopeFieldRule}，
 * 分别是必填字段规则、字典字段规则、数值范围字段规则和日期时间范围字段规则。
 * 必填字段规则需要在构建规则配置Configuration时传入{@link info.lostred.ruler.domain.ValidInfo}集合；
 * 字典字段规则需要在构建规则配置Configuration时传入一个关于字典类型与码值集合的映射{@link java.util.Map}</p>
 *
 * @see info.lostred.ruler.core.AbstractRule
 * @see info.lostred.ruler.rule.RequiredFieldRule
 * @see info.lostred.ruler.rule.DictFieldRule
 * @see info.lostred.ruler.core.ValidConfiguration
 * @see info.lostred.ruler.domain.ValidInfo
 */
package info.lostred.ruler.rule;