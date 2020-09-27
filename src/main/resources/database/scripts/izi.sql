CREATE TABLE `rol` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `rol_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rol_id` (`rol_id`),
  CONSTRAINT `fk_rol_id` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`)
) ;


insert into `Izi`.`rol`(id,code,name)
values(1,1,'Tutor'),(2,2,'Estudiante');

INSERT INTO `Izi`.`user`
(`id`,
`email`,
`first_name`,
`last_name`,
`status`,
`rol_id`)
VALUES
(1,
'tutor@gmail.com',
'nombreTutor',
'apellidoTutor',
'Status2',
1),
(2,
'estudiante1@gmail.com',
'estudiante1',
'estudiante1',
'Status2',
2),
(3,
'estudiante2@gmail.com',
'estudiante2',
'estudiante2',
'Status2',
2);

CREATE TABLE `operation_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `operation_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `visible` boolean NOT NULL,
  `visible_when_in_parameter_equals_condition` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `enable` boolean NOT NULL,
  `required` boolean NOT NULL,
  `validate_expression` varchar(255) NOT NULL,
  `validate_expression_error_description` varchar(255) NOT NULL,
  `date_format` varchar(255) NOT NULL,
  `source_value_entity_property` varchar(255) NOT NULL,
  `convert` boolean NOT NULL,
  `operation_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_operation_type_id` (`operation_type_id`),
  CONSTRAINT `fk_operation_type_id` FOREIGN KEY (`operation_type_id`) REFERENCES `operation_type` (`id`)
);

CREATE TABLE `operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `visible` boolean NOT NULL,
  `pre_execute` boolean NOT NULL,
  `comment` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `automatic` boolean NOT NULL,
  `operation_order` int(11) NOT NULL,
  `operation_type_id` bigint(20) DEFAULT NULL,
  `notify_alternative` boolean NOT NULL,
  `alternative_ids` varchar(255) NOT NULL,
  `notify_operation` boolean NOT NULL,
  `operation_notify_ids` varchar(255) NOT NULL,
  `guide_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_operation_type_id2` (`operation_type_id`),
  #KEY `fk_guide_id` (`guide_id`),
  CONSTRAINT `fk_operation_type_id2` FOREIGN KEY (`operation_type_id`) REFERENCES `operation_type` (`id`)
  #CONSTRAINT `fk_guide_id` FOREIGN KEY (`guide_id`) REFERENCES `guide` (`id`)
);

CREATE TABLE `convertion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `condition` varchar(255) NOT NULL,
  `sourceUnit` varchar(255) NOT NULL,
  `destinationUnit` varchar(255) NOT NULL,
  `operation_parameter_id` bigint(20) DEFAULT NULL,
  `convertion_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_operation_parameter_id` (`operation_parameter_id`),
  #KEY `fk_convertion_type_id` (`convertion_type_id`),
  #CONSTRAINT `fk_convertion_type_id` FOREIGN KEY (`convertion_type_id`) REFERENCES `convertion_type` (`id`),
  CONSTRAINT `fk_operation_parameter_id` FOREIGN KEY (`operation_parameter_id`) REFERENCES `operation_parameter` (`id`)
);


