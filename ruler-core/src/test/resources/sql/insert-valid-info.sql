REPLACE INTO 'tableName' (`id`, `business_type`, `valid_type`, `field_name`, `lower_limit`, `upper_limit`, `required`,
                          `enable`, `valid_class_name`)
VALUES ('1', 'common', 'required', 'string', null, null, true, true, 'com.ylzinfo.ruler.domain.model.SubValidClass')
     , ('2', 'common', 'required', 'number', null, null, true, true, 'com.ylzinfo.ruler.domain.model.SubValidClass')
     , ('3', 'common', 'required', 'time', null, null, true, true, 'com.ylzinfo.ruler.domain.model.SubValidClass')
     , ('4', 'common', 'dict', 'string', null, null, true, true, 'com.ylzinfo.ruler.domain.model.SubValidClass');
