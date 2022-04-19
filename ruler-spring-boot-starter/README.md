# 📐ruler-spring-boot-starter

## 🚀快速开始

### 引入依赖

```xml

<dependency>
    <groupId>info.lostred.ruler</groupId>
    <artifactId>ruler-spring-boot-starter</artifactId>
    <version>{ruler.version}</version>
</dependency>
```

### 配置application.yaml

框架默认只会根据application.yaml配置单实例规则引擎，项目中需要使用到多类规则引擎时，需要自己编写配置类。

```yaml
ruler:
  db-config:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/rules_engine
    username: rules_engine
    password: 123456
  default-business-type: common #业务类型，对应以下两张配置表的business_type，用于构建引擎时筛选对应的规则信息与校验信息，默认为common
  default-valid-class: info.lostred.ruler.domain.model.ValidClass #规则引擎所约束的java类型
  valid-config:
    init-type: annotation #校验信息的初始化方式，分为注解(annotation)和数据库(db)两种，默认为注解方式，注解方式需要手动配置校验信息，数据库方式需要引入数据库驱动包
    table-name: ruler_valid_info #校验信息配置表表名
  rule-config:
    init-type: annotation #规则信息的初始化方式，分为注解(annotation)和数据库(db)两种，默认为注解方式，注解方式需要手动配置规则信息，数据库方式需要引入数据库驱动包
    table-name: ruler_rule_info #规则信息配置表表名
    scan-base-packages: info.lostred.ruler.rule #规则包扫描路径
  rules-engine-config:
    type: complete #上述提到的规则引擎类型，默认为simple
```

或者配置spring的数据源

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/rules_engine
    username: rules_engine
    password: 123456
```

### 创建数据源及配置表(可选)

实际上，使用数据库的初始化方式并不需要手动执行sql脚本，程序运行后便会执行初始化sql语句。

```sql
CREATE TABLE IF NOT EXISTS ruler_rule_info
(
    `rule_code`       varchar(24) PRIMARY KEY COMMENT '规则编号',
    `business_type`   varchar(24) COMMENT '业务类型',
    `grade`           varchar(24) COMMENT '规则校验结果等级',
    `desc`            varchar(255) COMMENT '规则描述',
    `seq`             int(11) COMMENT '规则执行的顺序号',
    `required`        bit(1) COMMENT '是否强制使用',
    `enable`          bit(1) COMMENT '是否启用',
    `rule_class_name` varchar(128) COMMENT '规则实现类的全限定类名'
) COMMENT '规则信息配置表';

CREATE TABLE IF NOT EXISTS ruler_valid_info
(
    `id`               varchar(24) PRIMARY KEY COMMENT 'id',
    `business_type`    varchar(24) COMMENT '业务类型',
    `valid_type`       varchar(24) COMMENT '校验类型',
    `field_name`       varchar(32) COMMENT '校验字段名',
    `lower_limit`      decimal(16, 2) COMMENT '下限值',
    `upper_limit`      decimal(16, 2) COMMENT '上限值',
    `begin_time`       datetime COMMENT '开始时间',
    `end_time`         datetime COMMENT '结束时间',
    `required`         bit(1) COMMENT '是否强制使用',
    `enable`           bit(1) COMMENT '是否启用',
    `valid_class_name` varchar(128) COMMENT '校验类型的全限定类名'
) COMMENT '校验信息配置表';
```

valid_type的填写可参考ValidType枚举类，字母全大写。

### 编写配置类(可选)

使用注解初始化方式必须配置GlobalConfiguration，单实例规则引擎不能满足项目时，可自定义规则引擎。

```java

import info.lostred.ruler.core.ValidConfiguration;

@Configuration
@RuleScan("info.lostred.ruler.rule")
public class RulerConfig {
    private static final String validClassName = "info.lostred.ruler.domain.model.SubValidClass";
    private static final String businessType = RulerConstants.DEFAULT_BUSINESS_TYPE;

    //如果不使用数据库初始化方式，则需要在spring容器中注册一个ValidConfiguration实例对象
    @Bean
    public ValidConfiguration validConfiguration() {
        Collection<ValidInfo> validInfos = new ArrayList<>();
        ValidInfo validInfo1 = new ValidInfo("1", businessType, ValidType.REQUIRED.name(), "string", validClassName);
        ValidInfo validInfo2 = new ValidInfo("2", businessType, ValidType.REQUIRED.name(), "number", validClassName);
        ValidInfo validInfo3 = new ValidInfo("3", businessType, ValidType.REQUIRED.name(), "time", validClassName);
        ValidInfo validInfo4 = new ValidInfo("4", businessType, ValidType.DICT.name(), "string", validClassName);
        ValidInfo validInfo5 = new ValidInfo("5", businessType, ValidType.NUMBER_SCOPE.name(), "number", validClassName);
        validInfo5.setUpperLimit(BigDecimal.TEN);
        ValidInfo validInfo6 = new ValidInfo("6", businessType, ValidType.DATETIME_SCOPE.name(), "time", validClassName);
        validInfo6.setEndTime(LocalDateTime.now());
        validInfos.add(validInfo1);
        validInfos.add(validInfo2);
        validInfos.add(validInfo3);
        validInfos.add(validInfo4);
        validInfos.add(validInfo5);
        validInfos.add(validInfo6);
        ValidConfiguration validConfiguration = new ValidConfiguration(validInfos);
        Set<Object> set = new HashSet<>(Arrays.asList("hello", "world"));
        validInfo4.setDict(set);
        return validConfiguration;
    }

    //选择适合的规则引擎注册到spring容器
    @Bean
    public RulesEngine<ValidClass> rulerEngine(RuleFactory ruleFactory) {
        TypeReference<CompleteRulesEngine<ValidClass>> typeReference = new TypeReference<CompleteRulesEngine<ValidClass>>() {
        };
        return DefaultRulesEngineFactory.builder(ruleFactory, businessType, typeReference).build();
    }
}
```

以上，ValidClass为需要校验的类。

### 规则引擎依赖注入

```java

@SpringBootTest
class ApplicationTest {
    @Autowired
    RulesEngineFactory rulesEngineFactory;

    @Test
    void test() {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setNumber(new BigDecimal(11));
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));
        RulesEngine<ValidClass> rulesEngine = rulesEngineFactory.dispatch(RulerConstants.DEFAULT_BUSINESS_TYPE, validClass, ValidClass.class);
        Result result = rulesEngine.execute(validClass);
        System.out.println(result);
    }
}
```

这里注入的是RulesEngineFactory接口，使用该接口的dispatch()方法获取业务类型对应的规则引擎接口。当然也可以直接注入自己配置规则引擎的实现类。

## 💻二次开发

继承AbstractRule，重写接口方法即可实现自定义规则，若使用注解方式，需要在类上添加@Rule注解。

```java

@Rule(ruleCode = "test_1", businessType = "common", desc = "number必须>0", validClass = ValidClass.class)
public class NumberRule extends AbstractRule<ValidClass> {

    private final static String FIELD_NAME = "number";

    public NumberRule(GlobalConfiguration rulesEngineConfiguration, RuleInfo ruleInfo) {
        super(rulesEngineConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(ValidClass element) {
        return element.getNumber() != null;
    }

    @Override
    public boolean judge(ValidClass element) {
        return element.getNumber().intValue() <= 0;
    }

    @Override
    public Report buildReport(ValidClass element) {
        if (this.judge(element)) {
            return this.getReport(ruleInfo, element, FIELD_NAME, element.getNumber());
        }
        return null;
    }
}
```

### Rule注解

在类上标记该注解，规则工厂在扫描包时，会将其放入缓存。

### RuleScan注解

在配置类上标记该注解，规则工厂会扫描其value指定的包路径。当使用spring时，需将该配置类注册到spring容器。

```java

@Configuration
@RuleScan("info.lostred.ruler.rule")
public class RulerConfig {
}
```
