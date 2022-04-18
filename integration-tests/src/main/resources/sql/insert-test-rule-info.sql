REPLACE INTO ruler_rule_info (`rule_code`, `business_type`, `grade`, `desc`, `seq`, `required`, `enable`,
                              `rule_class_name`, `valid_class_name`)
VALUES ('test_1', 'test', 'ILLEGAL', '字段number>3时，string不能为test', 3, true, true, 'info.lostred.ruler.test.rule.StringRule',
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('test_2', 'test', 'SUSPECTED', '字段number必须>0', 4, true, true, 'info.lostred.ruler.test.rule.NumberRule',
        'info.lostred.ruler.test.domain.model.SubValidClass')
