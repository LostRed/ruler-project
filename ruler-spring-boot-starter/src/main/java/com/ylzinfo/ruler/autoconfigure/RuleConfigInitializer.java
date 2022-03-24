package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public interface RuleConfigInitializer extends InitializingBean {
    /**
     * 抓取校验信息
     *
     * @return 校验信息集合
     */
    List<ValidInfo> fetchValidInfo();

    /**
     * 抓取规则信息
     *
     * @return 校验规则集合
     */
    List<RuleInfo> fetchRuleInfo();
}
