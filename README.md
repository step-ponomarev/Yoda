# Yoda
To do list, a little home project.

create table if not exists task_box (
    id mediumint not null auto_increment,
    type char (10) not null,
    statement char (100) not null,
    time_of_creation datetime not null,
    time_of_last_change datetime not null,
    primary key (id)
                                    );
