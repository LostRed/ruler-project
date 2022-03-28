CREATE TABLE IF NOT EXISTS ruler_valid_info
(
    `id`               varchar(24) PRIMARY KEY COMMENT 'id',
    `business_type`    varchar(24) COMMENT '业务类型',
    `valid_type`       varchar(24) COMMENT '校验类型',
    `field_name`       varchar(32) COMMENT '校验字段名',
    `lower_limit`      decimal(16, 2) COMMENT '下限值',
    `upper_limit`      decimal(16, 2) COMMENT '上限值',
    `begin_time`       datetime COMMENT '开始时间',
    `end_time`         datetime COMMENT '结束时间',
    `required`         bit(1) COMMENT '是否强制使用',
    `enable`           bit(1) COMMENT '是否启用',
    `valid_class_name` varchar(128) COMMENT '校验类型的全限定类名'
) COMMENT '校验信息配置表'