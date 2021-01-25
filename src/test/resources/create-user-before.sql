delete
from user_role;
delete
from users;

insert into users(id, active, password, username)
values (1, true, '$2a$12$o9eBl/OdfY9nVxWzNWGlq.oHULqUd/FiO95QWiqRfGoyIQe85SV22', 'user1'),
       (2, true, '$2a$12$o9eBl/OdfY9nVxWzNWGlq.oHULqUd/FiO95QWiqRfGoyIQe85SV22', 'user2');

insert into user_role(user_id, roles)
values (1, 'USER'),
       (1, 'ADMIN'),
       (2, 'USER');