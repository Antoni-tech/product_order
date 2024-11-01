INSERT INTO public.user_role (id, role_id, user_id)
VALUES (1, (select id from roles where name = 'Admin'), (select id from users where login = 'admin'));
INSERT INTO public.user_role (id, role_id, user_id)
VALUES (2, (select id from roles where name = 'Moderator'), (select id from users where login = 'moder'));
INSERT INTO public.user_role (id, role_id, user_id)
VALUES (3, (select id from roles where name = 'User'), (select id from users where login = 'user'));
