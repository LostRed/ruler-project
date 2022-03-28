package com.ylzinfo.ruler.test.rule.extension;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.test.entity.User;

@Rule(ruleCode = "test", businessType = "user", desc = "密码必须大于6位", validClass = User.class)
public class PasswordLengthRule extends AbstractRule<User> {

    public PasswordLengthRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(User element) {
        return true;
    }

    @Override
    public boolean judge(User element) {
        return element.getPassword().length() < 6;
    }

    @Override
    public Report buildReport(User element) {
        if (this.judge(element)) {
            return this.getReport(ruleInfo, element, "password", element.getPassword());
        }
        return null;
    }
}
