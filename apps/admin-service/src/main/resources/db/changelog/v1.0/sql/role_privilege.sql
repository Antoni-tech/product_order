INSERT INTO role_privilege (id, role_id, privileges_id)
SELECT nextval('app_seq'), r.id, p.id
FROM roles r
         JOIN privileges p ON p.privilege_group = 'Roles'
WHERE r.name = 'Admin';

INSERT INTO role_privilege (id, role_id, privileges_id)
SELECT nextval('app_seq'), r.id, p.id
FROM roles r
         JOIN privileges p ON p.privilege_group = 'Users'
WHERE r.name = 'Admin';

INSERT INTO role_privilege (id, role_id, privileges_id)
SELECT nextval('app_seq'), r.id, p.id
FROM roles r
         JOIN privileges p ON p.privilege_group = 'Administrators'
WHERE r.name = 'Admin';

INSERT INTO role_privilege (id, role_id, privileges_id)
SELECT nextval('app_seq'), r.id, p.id
FROM roles r
         JOIN privileges p ON p.privilege_group = 'Settings'
WHERE r.name = 'Admin';

INSERT INTO role_privilege (id, role_id, privileges_id)
SELECT  nextval('app_seq'),(SELECT id FROM roles WHERE name = 'User'), p.id
FROM privileges p
WHERE p.privilege_group = ANY (ARRAY['Settings'])
  AND p.tag = 'view';