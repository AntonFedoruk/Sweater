create table message_likes (
    message_id      bigint not null,
    user_id         bigint not null,
    primary key (message_id, user_id),
    foreign key (message_id)    references messages (id),
    foreign key (user_id)       references users (id)
    ) engine = InnoDB;