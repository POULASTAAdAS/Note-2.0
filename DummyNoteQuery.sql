create database noteDatabase;
use noteDatabase;

create table noteTable(
_id int primary key auto_increment,
heading varchar(40),
content varchar(100),
updateData date,
edited int,
pinned bool,
syncState bool
);

drop table noteTable;

insert into noteTable values
(1 ,"heading" , "content" , '2008-11-09' , 40 , true , false),
(2 ,"heading" , "content" , '2009-11-09' , 10 , true , false),
(3 ,"heading" , "content" , '2008-11-19' , 0 , true , false),
(4 ,"heading" , "content" , '2002-11-19' , 0 , true , false),
(5 ,"heading" , "content" , '2008-11-09' , 0 , true , false),
(6 ,"heading" , "content" , '2008-11-09' , 10 , false , false),
(7 ,"heading" , "content" , '2011-11-09' , 110 , false , false),
(8 ,"heading" , "content" , '2004-11-09' , 20 , false , false),
(9 ,"heading" , "content" , '2012-11-01' , 0 , false , false),
(10 ,"heading" , "content" , '2005-11-12' , 0 , true , false),
(13 ,"this is heading" , "this is content" , '2021-11-29' , 0 , true , true),
(11 ,"heading" , "content" , '2021-11-29' , 0 , true , false),
(12 ,"heading" , "content" , '2023-11-19' , 50 , true , false);

select * from noteTable order by pinned desc , edited desc;

select * from noteTable order by pinned desc , updateData desc;

select * from noteTable order by edited desc;

select * from noteTable order by updateData desc;

select * from noteTable;

select * from noteTable where heading like "%this%" or content like "%c%" order by updateData desc;

select * from noteTable where _id in (1,2,3);

update noteTable set syncState = true where _id = 1;

delete from noteTable where _id = 1;

update noteTable set pinned = if(pinned , 0 ,1) where _id = 1;

update noteTable set pinned = case when pinned = 1 then 0 else 1 end where _id=1;




