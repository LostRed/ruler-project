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
  rule-default-scope: info.lostred.ruler.test.rule #规则类包扫描路径，与注解@RulerScan定义的路径会取并集并一起扫描
  domain-default-scope: info.lostred.ruler.test.entity #领域模型类包扫描路径，与注解@DomainScan定义的路径会取并集并一起扫描
```

### 编写配置类(可选)

使用注解初始化方式必须配置GlobalConfiguration，单实例规则引擎不能满足项目时，可自定义规则引擎。

```java
import info.lostred.ruler.autoconfigure.RulerProperties;
import info.lostred.ruler.core.ValidConfiguration;

@Configuration
@RuleScan("info.lostred.ruler.test.rule")
@DomainScan("info.lostred.ruler.test.entity")
public class RulerConfig {
}
```

以上，Person为需要校验的类，下面是实体类的示例代码。

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
class ApplicationTest {
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
        Date parse = simpleDateFormat.parse("2020-01-01");
        person.setBirthday(parse);
        Area area = new Area();
        person.setArea(area);
        Contact contact1 = new Contact();
        contact1.setArea(area);
        contact1.setPassword("1234");
        Contact contact2 = new Contact();
        contact2.setPassword("1234");
        person.setContacts(Arrays.asList(contact1, contact2));
    }

    @Test
    void rulesEngineFactoryTest() throws JsonProcessingException {
        RulesEngine rulesEngine = rulesEngineFactory.getEngine(businessType);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(person);
        long e = System.currentTimeMillis();
        printResult(result, s, e);
    }
}
```

这里注入的是RulesEngineFactory接口，使用该接口的dispatch()方法获取业务类型对应的规则引擎接口。当然也可以直接注入自己配置规则引擎的实现类。

## 💻二次开发

继承AbstractRule，重写接口方法即可实现自定义规则，若使用注解方式，需要在类上添加@Rule注解。

```java
@Rule(ruleCode = "rule_01",
        businessType = "person",    //自定义的业务类型
        description = "身份证号码长度必须为18位",
        parameterExp = "certNo",
        conditionExp = "certNo!=null",
        predicateExp = "certNo.length()!=18")
public class CertNoLengthRule extends AbstractRule {
    public CertNoLengthRule(RuleDefinition ruleDefinition, ExpressionParser parser) {
        super(ruleDefinition, parser);
    }
}
```

### Rule注解

在类上标记该注解，规则工厂在扫描包时，会将其放入RuleFactory的单例池，统一管理。

### RuleScan注解

在配置类上标记该注解，规则工厂会扫描其value指定的包路径。当使用spring时，需将该配置类注册到spring容器。

### DomainScan注解

在配置类上标记该注解，规则工厂会扫描其value指定的包路径。当使用spring时，需将该配置类注册到spring容器。

```java
@Configuration
@RuleScan("info.lostred.ruler.test.rule")
@DomainScan("info.lostred.ruler.test.entity")
public class RulerConfig {
}
```
