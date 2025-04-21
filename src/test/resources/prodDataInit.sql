DELETE FROM products;

INSERT INTO products ( name, description, price, created_at, updated_at)
VALUES ( 'Lily', 'Flowers', 22.08, now(), now()),
       ( 'Ground', 'Essential', 10.08, now(), now());