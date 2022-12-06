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
  business-type: person #业务类型
  engine-type: complete #上述提到的规则引擎类型，默认为simple
  rule-default-scope: info.lostred.ruler.test.rule #规则类包扫描路径，与注解@RuleScan定义的路径会取并集并一起扫描
  domain-default-scope: info.lostred.ruler.test.domain #领域模型类包扫描路径，与注解@DomainScan定义的路径会取并集并一起扫描
```

### 编写配置类(可选)

使用注解初始化方式必须配置Configuration，单实例规则引擎不能满足项目时，可自定义规则引擎。

```java
@Configuration
@RuleScan("info.lostred.ruler.test.rule")
@DomainScan("info.lostred.ruler.test.domain")
public class RulerConfig {
    //注册全局函数，spEl表达式可以使用#methodName调用全局函数
    @Bean
    public List<Method> globalFunctions() {
        return Arrays.asList(DateTimeUtils.class.getMethods());
    }
}
```

以上，info.lostred.ruler.test.domain为需要校验类的包名路径，下面是需要校验类的示例代码。

```java
@Data
public class Person {
    private String certNo;
    private String name;
    private String gender;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    private Area area;
    private List<Contact> contacts;
}

@Data
public class Area {
    private String continent;
    private String country;
    private String province;
    private String city;
    private String district;
    private String town;
}

@Data
public class Contact {
    private String type;
    private String account;
    private String password;
    private Area area;
}
```

### 规则引擎依赖注入

以下是单元测试案例。

```java
@SpringBootTest
class RulesEngineTest {
    static String businessType = "person";
    static Person person;
    @Autowired
    RulesEngineFactory rulesEngineFactory;
    @Autowired
    ObjectMapper objectMapper;

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    void printResult(Object result, long startTime, long endTime) throws JsonProcessingException {
        System.out.println(toJson(result));
        System.out.println("执行时间: " + (endTime - startTime) + " ms");
    }

    @BeforeAll
    static void init() throws ParseException {
        person = new Person();
        person.setCertNo("12312");
        person.setGender("男");
        person.setAge(10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("2019-01-01");
        person.setBirthday(parse);
        Area area = new Area();
        person.setArea(area);
        Contact contact1 = new Contact();
        contact1.setArea(area);
        contact1.setType("sdf");
        contact1.setPassword("1234");
        Contact contact2 = new Contact();
        contact2.setPassword("1234");
        person.setContacts(Arrays.asList(contact1, contact2));
    }

    @Test
    void executeTest() throws JsonProcessingException {
        RulesEngine rulesEngine = rulesEngineFactory.getEngine(businessType);
        long s = System.currentTimeMillis();
        EvaluationContext context = rulesEngine.createEvaluationContext(person);
        rulesEngine.execute(context);
        long e = System.currentTimeMillis();
        Result result = rulesEngine.getResult(context);
        printResult(result, s, e);
    }
}
```

这里注入的是RulesEngineFactory接口，使用该接口的getEngine()方法获取业务类型对应的规则引擎接口。当然也可以直接注入自己配置规则引擎的实现类。

## 💻规则开发

继承AbstractRule，并在类上添加@Rule注解。以下提供了开发规则两种方式。

1. 继承DeclarativeRule，采用声明式开发，使用注解直接配置表达式

```java
import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.DeclarativeRule;

@Rule(ruleCode = "身份证号码长度",
        businessType = "person",
        description = "身份证号码长度必须为18位",
        parameterExp = "certNo",
        conditionExp = "certNo!=null",
        predicateExp = "certNo.length()!=18")
public class CertNoLengthRule extends DeclarativeRule {
    public CertNoLengthRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
```

2. 继承ProgrammaticRule，采用编程式开发，重写ProgrammaticRule的方法

```java
import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.ProgrammaticRule;

@Rule(ruleCode = "姓名必填",
        businessType = "person",
        description = "姓名不能为空")
public class NameRule extends ProgrammaticRule<Person> {
    public NameRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    public Object getInitValue(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return person.getName();
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return !ObjectUtils.isEmpty(person);
    }

    @Override
    public boolean evaluate(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return ObjectUtils.isEmpty(person.getName());
    }
}
```

### Rule注解

在类上标记该注解，规则工厂在扫描包时，会将其放入RuleFactory的单例池，统一管理。同时RuleFactory规则工厂提供了动态注册规则的方法registerRuleDefinition，通过该方法可以动态地将声明好的规则定义注册到规则工厂。
需要注意的是，注册到规则工厂的规则并不会立即被规则引擎所使用，还需要调用RulesEngineFactory规则引擎工厂的reloadRules方法，才能将所有引擎中的方法重新初始化。

### RuleScan注解

在配置类上标记该注解，规则工厂会扫描其value指定的包路径。当使用spring时，需将该配置类注册到spring容器。

### DomainScan注解

在配置类上标记该注解，规则工厂会扫描其value指定的包路径。当使用spring时，需将该配置类注册到spring容器。

```java
@Configuration
@RuleScan("info.lostred.ruler.test.rule")
@DomainScan("info.lostred.ruler.test.domain")
public class RulerConfig {
}
```
