-- Записи для разрешений в разделе "Users"
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (1, current_date, 'Create users', 'create', 'Users');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (2, current_date, 'Remove users', 'remove', 'Users');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (3, current_date, 'Edit users', 'edit', 'Users');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (4, current_date, 'View users', 'view', 'Users');

-- Записи для разрешений в разделе "Roles"
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (5, current_date, 'Create roles', 'create', 'Roles');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (6, current_date, 'Remove roles', 'remove', 'Roles');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (7, current_date, 'Edit roles', 'edit', 'Roles');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (8, current_date, 'View roles', 'view', 'Roles');

-- Записи для разрешений в разделе "Administrators"
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (9, current_date, 'Create administrators', 'create', 'Administrators');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (10, current_date, 'Remove administrators', 'remove', 'Administrators');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (11, current_date, 'Edit administrators', 'edit', 'Administrators');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (12, current_date, 'View administrators', 'view', 'Administrators');

-- Записи для разрешений в разделе "Settings"
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (13, current_date, 'Create settings', 'create', 'Settings');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (14, current_date, 'Remove settings', 'remove', 'Settings');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (15, current_date, 'Edit settings', 'edit', 'Settings');
INSERT INTO public.privileges (id, created_date, name, tag, privilege_group) VALUES (16, current_date, 'View settings', 'view', 'Settings');