insert into users (id, username, password, active)
values (1, 'admin', '$2y$12$bKxsaCDEL4wQ1hi9ipPlyuWsRjVEPUTyiFO6eTjxxn8Nx2YvUCA/i', true);

insert into user_role (user_id, roles)
values (1, 'ADMIN'), (1, 'USER');