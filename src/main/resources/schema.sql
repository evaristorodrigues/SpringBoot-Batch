drop table if exists PERSON;
create table PERSON(
 id bigint(10) not null auto_increment primary key,
 email varchar(255) not null,
 age int(3) not null,
 first_name varchar(255) not null
 );
