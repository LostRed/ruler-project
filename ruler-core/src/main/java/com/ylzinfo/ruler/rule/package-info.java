/**
 * <h2>抽象规则{@link com.ylzinfo.ruler.core.AbstractRule}的实现类包</h2>
 * <p>工程默认提供四个实现类：{@link com.ylzinfo.ruler.rule.RequiredFieldRule}、
 * {@link com.ylzinfo.ruler.rule.DictFieldRule}、{@link com.ylzinfo.ruler.rule.NumberScopeFieldRule}、
 * {@link com.ylzinfo.ruler.rule.DateTimeScopeFieldRule}，
 * 分别是必填字段规则、字典字段规则、数值范围字段规则和日期时间范围字段规则。
 * 必填字段规则需要在构建规则配置Configuration时传入{@link com.ylzinfo.ruler.domain.ValidInfo}集合；
 * 字典字段规则需要在构建规则配置Configuration时传入一个关于字典类型与码值集合的映射{@link java.util.Map}</p>
 *
 * @see com.ylzinfo.ruler.core.AbstractRule
 * @see com.ylzinfo.ruler.rule.RequiredFieldRule
 * @see com.ylzinfo.ruler.rule.DictFieldRule
 * @see com.ylzinfo.ruler.core.GlobalConfiguration
 * @see com.ylzinfo.ruler.domain.ValidInfo
 */
package com.ylzinfo.ruler.rule;