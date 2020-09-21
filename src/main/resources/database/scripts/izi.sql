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

