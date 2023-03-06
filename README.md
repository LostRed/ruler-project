# 📏Ruler

## 1️⃣特性

- 框架侵入性低，可扩展性强
- 一处配置，处处使用
- 可根据需求配置不同的引擎类型(simple, incomplete, complete)

## 2️⃣核心概念

### DomainFactory领域模型工厂

- 根据配置解析规则引擎需要校验类的属性
- 统一管理这些需要校验的类
- 在动态生成规则时能够更好地提供表达式片段的支持

### RulesEngineFactory规则引擎工厂

- 根据配置构建规则引擎
- 统一管理不同业务类型的规则引擎

### RuleFactory规则工厂

- 根据配置构建规则
- 统一管理规则

### RulesEngine接口

- simple类型的实现类只关心结果，有违规则直接返回结果
- incomplete类型的实现类会输出报告，有违规也会直接返回结果
- complete类型的实现类会输出报告，并且会执行完所有的规则

### AbstractRule抽象类

基于spring的spEL表达式实现主要逻辑功能，定义了规则的主要方法，开发者可扩展该类，实现其他特殊的规则。

## 3️⃣核心接口关系

- Domain通常为RulesEngine中execute方法的入参，通过RulesEngine中所有Rule规则执行后得到一个对应的Result结果。
- Rule统一由RuleFactory管理，这些规则与规则引擎之间依靠businessType属性联系

<img src="https://cdn.jsdelivr.net/gh/LostRed/pic-repository@master/ruler-project.72lfu3ibjfg0.webp" alt="">
