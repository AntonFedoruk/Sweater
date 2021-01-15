create table hibernate_sequence(
    next_val bigint
) engine = InnoDB;

insert into hibernate_sequence values (1);
insert into hibernate_sequence values (1);

create table messages(
    id       bigint        not null,
    filename varchar(255),
    tag      varchar(255),
    text     varchar(2048) not null,
    user_id  bigint,
    primary key (id)
) engine = InnoDB;

create table user_role(
    user_id bigint not null,
    roles   varchar(20)
) engine = InnoDB;

create table users(
    id              bigint       not null,
    activation_code varchar(50),
    active          boolean      not null,
    email           varchar(255),
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)
) engine = InnoDB;

alter table messages add constraint FK_MESSAGES_USER foreign key (user_id) references users (id);
alter table user_role add constraint FK_USER_ROLE_USER foreign key (user_id) references users (id);