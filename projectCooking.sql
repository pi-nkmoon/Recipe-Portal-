create database CookingRecipePortal;
use CookingRecipePortal;

create table chef (chefID int , chefName varchar(30) , chefRating numeric(2,1) , primary key(chefID));
create table recipe (recipeID int , chefID int ,recipeName varchar(30), likes int,dislikes int,primary key(recipeID), FOREIGN KEY (chefID) REFERENCES chef(chefID) ON DELETE CASCADE ON update CASCADE);
create table linker (chefID int , recipeID int ,ingredient varchar(20) , FOREIGN KEY (chefID) REFERENCES chef(chefID) ON DELETE CASCADE ON update CASCADE , 
FOREIGN KEY (recipeID) REFERENCES recipe(recipeID) ON DELETE CASCADE ON update CASCADE);

insert into chef values (1,'Ranveer',0);
insert into chef values (2,'Sanjeev',0);
insert into chef values (3,'Gordon',0);
insert into chef values (4,'Butch',0);
insert into chef values (5,'Marcellus',0);

insert into recipe values (1,5,'Dum Aaloo',0,0);
insert into recipe values (2,5,'Biryani',0,0);
insert into recipe values (3,3,'Pizza',0,0);
insert into recipe values (4,5,'Burger',0,0);
insert into recipe values (5,4,'Khandeshi Chicken',0,0);
insert into recipe values (6,2,'Mutton korma',0,0);
insert into recipe values (7,3,'Lasagne',0,0);
insert into recipe values (8,3,'Macroni',0,0);
insert into recipe values (9,1,'Aaloo parantha',0,0);
insert into recipe values (10,1,'Rajma chawal',0,0);

insert into linker values (1,9,'Aaloo');
insert into linker values (1,9,'wheat');
insert into linker values (1,10,'Rajma');
insert into linker values (1,10,'Rice');
insert into linker values (2,6,'Freshly cut mutton');
insert into linker values (2,6,'Garam masala');
insert into linker values (2,6,'Curd');
insert into linker values (3,3,'Dough');
insert into linker values (3,3,'Cheese');
insert into linker values (3,3,'Capsicum');
insert into linker values (3,7,'Cheese');
insert into linker values (3,7,'White wine');
insert into linker values (3,7,'Ground beef');
insert into linker values (3,8,'Macroni');
insert into linker values (3,8,'Cheese');
insert into linker values (3,8,'Milk');
insert into linker values (4,5,'Chicken legs');
insert into linker values (4,5,'Khandeshi Masala');
insert into linker values (5,1,'Aaloo');
insert into linker values (5,1,'Curd');
insert into linker values (5,1,'Garam Masala');
insert into linker values (5,2,'Rice');
insert into linker values (5,2,'Chicken legs');
insert into linker values (5,4,'Bread Buns');
insert into linker values (5,4,'Cheese');

delimiter //
create trigger Rater1 
after 
update
on recipe
for each row 
begin
set @likes:=(select sum(likes) from (select * from chef natural join recipe) as newtable where chefID=1 group by chefName);
set @dislikes:= (select sum(dislikes) from (select * from chef natural join recipe) as newtable where chefID=1 group by chefName);
update chef
SET chefRating = @likes*5/(@likes + @dislikes)
where chefID = 1;
end;//
delimiter ;

delimiter //
create trigger Rater2 
after 
update
on recipe
for each row 
begin
set @likes:=(select sum(likes) from (select * from chef natural join recipe) as newtable where chefID=2 group by chefName);
set @dislikes:= (select sum(dislikes) from (select * from chef natural join recipe) as newtable where chefID=2 group by chefName);
update chef
SET chefRating = @likes*5/(@likes + @dislikes)
where chefID = 2;
end;//
delimiter ;

delimiter //
create trigger Rater3 
after 
update
on recipe
for each row 
begin
set @likes:=(select sum(likes) from (select * from chef natural join recipe) as newtable where chefID=3 group by chefName);
set @dislikes:= (select sum(dislikes) from (select * from chef natural join recipe) as newtable where chefID=3 group by chefName);
update chef
SET chefRating = @likes*5/(@likes + @dislikes)
where chefID = 3;
end;//
delimiter ;

delimiter //
create trigger Rater4 
after 
update
on recipe
for each row 
begin
set @likes:=(select sum(likes) from (select * from chef natural join recipe) as newtable where chefID=4 group by chefName);
set @dislikes:= (select sum(dislikes) from (select * from chef natural join recipe) as newtable where chefID=4 group by chefName);
update chef
SET chefRating = @likes*5/(@likes + @dislikes)
where chefID = 4;
end;//
delimiter ;

delimiter //
create trigger Rater5
after 
update
on recipe
for each row 
begin
set @likes:=(select sum(likes) from (select * from chef natural join recipe) as newtable where chefID=5 group by chefName);
set @dislikes:= (select sum(dislikes) from (select * from chef natural join recipe) as newtable where chefID=5 group by chefName);
update chef
SET chefRating = @likes*5/(@likes + @dislikes)
where chefID = 5;
end;//
delimiter ;
