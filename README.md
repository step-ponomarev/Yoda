# Yoda
To do list, a little home project.

create table if not exists project_list (
  id char(36) not null,
  name varchar(100),
  time_of_creation datetime not null,
  time_of_last_change datetime not null,
  primary key (id)
                                        );

create table if not exists task_box (
    id char(36) not null,
    type varchar(10) not null,
    statement varchar(300) not null,
    time_of_creation datetime not null,
    time_of_last_change datetime not null,
    projectID char(36),
    foreign key (projectID) references project_list(id),
    primary key (id)
                                    );