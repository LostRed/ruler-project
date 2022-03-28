package com.ylzinfo.ruler;

import com.ylzinfo.ruler.utils.PackageScanUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

public class ScanTest {
    @Test
    void test() throws IOException {
        Set<String> mysql = PackageScanUtils.findClassNames("com.ylzinfo");
        mysql.forEach(System.out::println);
    }
}
