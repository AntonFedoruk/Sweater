insert into users (id, username, password, active)
values (1, 'admin', '$2y$12$zCL2dY4rNzhENrL0QYjoOuhMYp4CoaJyFg0Mc5DtQ3orxpUWPMfCS', true);

insert into user_role (user_id, roles)
values (1, 'ADMIN'), (1, 'USER');