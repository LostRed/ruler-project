# ruler-spring-boot-starter

## 快速开始

### 引入依赖

```xml

<dependency>
    <groupId>com.ylzinfo.ruler</groupId>
    <artifactId>ruler-spring-boot-starter</artifactId>
    <version>{ruler.version}</version>
</dependency>
```

### 创建数据源及配置表

```sql
CREATE TABLE IF NOT EXISTS 'tableName'
(
    `rule_code`       varchar(24) PRIMARY KEY COMMENT '规则编号',
    `business_type`   varchar(8) COMMENT '业务类型',
    `grade`           varchar(8) COMMENT '规则校验结果等级',
    `desc`            varchar(255) COMMENT '规则描述',
    `seq`             int(11) COMMENT '规则执行的顺序号',
    `required`        bit(1) COMMENT '是否强制使用',
    `enable`          bit(1) COMMENT '是否启用',
    `rule_class_name` varchar(128) COMMENT '规则实现类全限定类名'
) COMMENT '规则信息配置表';

CREATE TABLE IF NOT EXISTS 'tableName'
(
    `id`               varchar(24) PRIMARY KEY COMMENT 'id',
    `business_type`    varchar(8) COMMENT '业务类型',
    `valid_type`       varchar(8) COMMENT '校验类型',
    `field_name`       varchar(32) COMMENT '校验字段名',
    `lower_limit`      decimal(16, 2) COMMENT '下限值',
    `upper_limit`      decimal(16, 2) COMMENT '上限值',
    `required`         bit(1) COMMENT '是否强制使用',
    `enable`           bit(1) COMMENT '是否启用',
    `valid_class_name` varchar(128) COMMENT '校验类型的全限定类名'
) COMMENT '校验信息配置表';
```

### 配置application.yaml

框架默认只会根据application.yaml配置单实例规则引擎，项目中需要使用到多类规则引擎时，需要自己编写配置类。

```yaml
ruler:
  default-business-type: common #业务类型，对应以上两张配置表的business_type，用于构建引擎时筛选对应的规则信息与校验信息
  default-valid-class: com.ylzinfo.ruler.domain.model.ValidClass #规则引擎所约束的java类型
  default-rules-engine-type: complete #上述提到的规则引擎类型
  config-table-init: true #是否开启配置表的初始化功能，默认开启
```

### 编写配置类

单实例规则引擎不能满足项目时，可自定义规则引擎。

```java
@Configuration
public class Config {
    @Bean
    public RulesEngine<ValidClass> defaultRulesEngine() {
        List<Rule<ValidClass>> rules = new ArrayList<>();
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setRuleCode("test_1");
        ruleInfo.setBusinessType("common");
        ruleInfo.setGrade("可疑");
        ruleInfo.setDesc("测试规则");
        ruleInfo.setSeq(0);
        ruleInfo.setEnable(true);
        ruleInfo.setRuleClassName("com.ylzinfo.ruler.rule.TestRule");
        ValidConfiguration validConfiguration = new ValidConfiguration();
        //构建规则信息列表...
        rules.add(new DictFieldRule<>(validConfiguration, ruleInfo));
        //定义规则引擎类型与校验对象类型
        TypeReference<SimpleRulesEngine<ValidClass>> typeReference = new TypeReference<SimpleRulesEngine<ValidClass>>() {
        };
        return RulesEngineFactory.builder(typeReference, rules).build();
    }
}
```

ValidClass为校验对象的类。

### 规则引擎依赖注入

```java

@SpringBootTest
class ApplicationTests {
    @Autowired
    RulesEngineManager rulesEngineManager;

    @Test
    void test() {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setNumber(new BigDecimal(11));
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));
        RulesEngine<ValidClass> common = rulesEngineManager.dispatch("common", validClass, ValidClass.class);
        if (common instanceof DetailRulesEngine) {
            Result result = ((DetailRulesEngine<ValidClass>) common).execute(validClass);
            System.out.println(result);
        }
    }
}
```

这里注入的是RulesEngineManager接口，使用该接口的dispatch()方法获取业务类型对应的规则引擎接口。
当然也可以直接注入自己配置规则引擎的实现类。