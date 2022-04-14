REPLACE INTO ruler_valid_info (`id`, `business_type`, `valid_type`, `field_name`, `lower_limit`, `upper_limit`,
                               `begin_time`, `end_time`, `required`, `enable`, `valid_class_name`)
VALUES ('1', 'common', 'required', 'string', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('2', 'common', 'required', 'number', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('3', 'common', 'required', 'time', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('4', 'common', 'dict', 'string', null, null, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('5', 'common', 'number_scope', 'number', null, 10, null, null, true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass')
     , ('6', 'common', 'datetime_scope', 'time', null, null, null, '2022-03-25 00:00:00', true, true,
        'info.lostred.ruler.test.domain.model.SubValidClass');
