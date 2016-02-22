set foreign_key_checks = 0;

drop table if exists Recepie;
drop table if exists Ingredient;
drop table if exists RecepieIngredient;
drop table if exists Pallet;
drop table if exists OrderedPallets;
drop table if exists OrderContent;
drop table if exists Order;
drop table if exists Customer;
set foreign_key_checks = 1;

create table Recepie(
  recepieName varchar(20),
  primary key(name)
);

create table Ingredient(
  IngredientName varchar(20),
  stock int,
  deliveryDate date,
  deliveryAmount int,
  primary key(IngredientName)
);

create table RecepieIngredient(
  recepieName varchar(20),
  IngredientName varchar(20),
  amount decimal(5, 2),
  primary key(IngredientName, recepieName),
  foreign key(IngredientName) references Ingredient(IngredientName),
  foreign key(recepieName) references Recepie(recepieName)
);

create table Pallet(
  palletId int AUTO_INCREMENT,
  prodDate date,
  recepieName varchar(20),
  status varchar(10),
  blocked boolean,
  primary key(palletId),
  foreign key(recepieName) references Recepie(recepieName)
);

create table OrderedPallets(
  orderId int,
  palletId int,
  primary key(orderId, palletId),
  foreign key(orderId) references Order(orderId),
  foreign key(palletId) references Pallet(palletId)
);
create table OrderContent(
  orderId int ,
  type varchar(20),
  amount int,
  primary key(orderId, type),
  foreign key(orderId) references Order(orderId)
);

create table Order(
  orderId int AUTO_INCREMENT,
  status varchar(10),
  deliveryDate date,
  primary key(orderId)
);

create table Customer(
  name varchar(20),
  adress varchar(20),
  primary key(name, adres)
);

insert into Recepie values ("Nut ring");
insert into Recepie values ("Nut cookie");
insert into Recepie values ("Amneris");
insert into Recepie values ("Almond delight");
insert into Recepie values ("Berliner");
