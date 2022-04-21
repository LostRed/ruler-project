REPLACE INTO ruler_valid_info (`id`, `business_type`, `valid_type`, `field_name`, `lower_limit`, `upper_limit`,
                               `begin_time`, `end_time`, `required`, `enable`, `valid_class_name`)
VALUES ('1', 'test', 'REQUIRED', 'string', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('2', 'test', 'REQUIRED', 'number', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('3', 'test', 'REQUIRED', 'time', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('4', 'test', 'DICT', 'string', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('5', 'test', 'NUMBER_SCOPE', 'number', null, 10, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('6', 'test', 'DATETIME_SCOPE', 'time', null, null, null, '2022-03-25 00:00:00', true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass');
