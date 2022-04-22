package info.lostred.ruler.test;

import info.lostred.ruler.domain.PropertyInfo;
import info.lostred.ruler.factory.DomainFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DomainFactoryTest {
    @Autowired
    DomainFactory domainFactory;

    @Test
    void test(){
        List<PropertyInfo> propertyList = domainFactory.getPropertyList("info.lostred.ruler.test.entity.Person");
        System.out.println(propertyList);
    }
}
