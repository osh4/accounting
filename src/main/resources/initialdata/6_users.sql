INSERT INTO user_roles(pk, f_name)
VALUES  (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO users (pk, f_login, f_firstname, f_lastname)
VALUES (1, 'testUser', 'test', 'user'),
       (2, 'testAdmin', 'test', 'admin');

INSERT INTO users_roles_rel(pk, f_user, f_role)
VALUES (1, 1, 1),
       (2, 2, 2)
