# Yoda
To do list, a little home project.

create table if not exists box_inbox (id mediumint not null auto_increment, date_of_creation DATE not null, statement char (100) not null, primary key (id));
create table if not exists box_today (id mediumint not null auto_increment, date_of_creation DATE not null, statement char (100) not null, primary key (id));
create table if not exists box_week (id mediumint not null auto_increment, date_of_creation DATE not null, statement char (100) not null, primary key (id));
create table if not exists box_late (id mediumint not null auto_increment, date_of_creation DATE not null, statement char (100) not null, primary key (id));