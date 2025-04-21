DELETE
FROM users;

INSERT INTO users (name, password, email, phone, user_role)
VALUES ('alex', 'secret', 'alex@example.com', '12345678', 'CLIENT');
INSERT INTO users (name, password, email, phone, user_role)
VALUES ('john', 'secret', 'john@example.com', '00112233', 'CLIENT');
