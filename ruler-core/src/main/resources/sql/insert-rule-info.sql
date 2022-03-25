REPLACE INTO ruler_rule_info (`rule_code`, `business_type`, `grade`, `desc`, `seq`, `required`, `enable`, `rule_class_name`)
VALUES ('common_1', 'common', '违规', '规定的字段必须填写', 0, true, true, 'com.ylzinfo.ruler.rule.RequiredFieldRule')
     , ('common_2', 'common', '违规', '规定的字段必须填写字典中的值', 1, true, true, 'com.ylzinfo.ruler.rule.DictFieldRule')
     , ('common_3', 'common', '违规', '规定的字段必须在限定的范围内', 2, true, true, 'com.ylzinfo.ruler.rule.NumberScopeFieldRule')
