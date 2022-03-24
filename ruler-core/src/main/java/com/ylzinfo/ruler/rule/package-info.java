/**
 * <h2>抽象规则{@link com.ylzinfo.ruler.core.Rule}的实现类包</h2>
 * <p>工程默认提供两个实现类：{@link com.ylzinfo.ruler.rule.RequiredFieldRule}和
 * {@link com.ylzinfo.ruler.rule.DictFieldRule}，
 * 分别是必填字段规则与字典字段规则。
 * 必填字段规则需要在构建规则配置Configuration时传入{@link com.ylzinfo.ruler.domain.ValidInfo}集合；
 * 字典字段规则需要在构建规则配置Configuration时传入一个关于字典类型与码值集合的映射{@link java.util.Map}</p>
 *
 * @see com.ylzinfo.ruler.core.Rule
 * @see com.ylzinfo.ruler.rule.RequiredFieldRule
 * @see com.ylzinfo.ruler.rule.DictFieldRule
 * @see com.ylzinfo.ruler.core.ValidConfiguration
 * @see com.ylzinfo.ruler.domain.ValidInfo
 */
package com.ylzinfo.ruler.rule;