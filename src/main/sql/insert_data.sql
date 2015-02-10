
INSERT INTO users(username,password,enabled)
VALUES ('admin','123', TRUE);
 
INSERT INTO user_roles (username, ROLE)
VALUES ('admin', 'ROLE_USER');
INSERT INTO user_roles (username, ROLE)
VALUES ('admin', 'ROLE_ADMIN');
