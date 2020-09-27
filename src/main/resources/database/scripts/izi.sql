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
values(1,1,'Tutor'),(2,2,'Estudiante')

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

CREATE TABLE `component_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `component_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `parameter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_parameter_id ` (`parameter_id`),
  CONSTRAINT `fk_parameter_id` FOREIGN KEY (`parameter_id`) REFERENCES `component_parameter` (`id`)
);


CREATE TABLE `component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_type_id ` (`type_id`),
  CONSTRAINT `fk_type_id` FOREIGN KEY (`type_id`) REFERENCES `component_type` (`id`)
);

CREATE TABLE `component_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `component_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_component_id ` (`component_id`),
  CONSTRAINT `fk_component_id` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`)
);
