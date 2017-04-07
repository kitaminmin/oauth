set names utf8;
set foreign_key_checks=0;

drop database if exists oauth;
create database oauth;
use oauth;

drop table if exists oauth;

create table oauth(
oauth_id int not null,
oauth_name varchar(255),
oauth_account varchar(255)
);