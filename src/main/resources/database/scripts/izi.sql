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

