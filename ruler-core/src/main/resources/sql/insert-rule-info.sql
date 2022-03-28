REPLACE INTO ruler_rule_info (`rule_code`, `business_type`, `grade`, `desc`, `seq`, `required`, `enable`,
                              `rule_class_name`, `valid_class_name`)
VALUES ('common_1', 'common', '违规', '规定的字段必须填写', 0, true, true, 'com.ylzinfo.ruler.rule.RequiredFieldRule',
        'java.lang.Object')
     , ('common_2', 'common', '违规', '规定的字段必须填写字典中的值', 1, true, true, 'com.ylzinfo.ruler.rule.DictFieldRule',
        'java.lang.Object')
     , ('common_3', 'common', '违规', '规定的数值字段必须在限定的范围内', 2, true, true, 'com.ylzinfo.ruler.rule.NumberScopeFieldRule',
        'java.lang.Object')
     , ('common_4', 'common', '违规', '规定的日期时间字段必须在限定的范围内', 3, true, true,
        'com.ylzinfo.ruler.rule.DatetimeScopeFieldRule', 'java.lang.Object')
