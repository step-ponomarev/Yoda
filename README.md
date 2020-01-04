# Yoda
To do list, a little home project.

create table if not exists box (id mediumint not null auto_increment, date_of_creation DATE not null, statement char
    (100) not null, type char (10) not null, primary key (id));
