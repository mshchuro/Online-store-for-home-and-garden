-- USERS
INSERT INTO Users (name, email, phone, password, role)
VALUES ('Alice Green', 'alice@example.com', '1234567890',
        '$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u', 'CLIENT'),
       ('Bob Gardener', 'bob@example.com', '0987654321', '$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u',
        'CLIENT'),
       ('Admin Joe', 'admin@example.com', '5555555555', '$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u',
        'ADMINISTRATOR');

-- CATEGORIES
INSERT INTO Categories (name)
VALUES ('Fertilizer'),
       ('Protective products and septic tanks'),
       ('Tools and equipment'),
       ('Pots and planters');

-- PRODUCTS
INSERT INTO Products (name, description, price, category_id, image_url, discount_price, created_at, updated_at)
VALUES ('Organic Compost', 'Rich organic compost for improving soil fertility', 12.99, 1, 'images/compost.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Slug Repellent', 'Eco-friendly slug and snail protection for gardens', 7.50, 2, 'images/slug_repellent.jpg',
        5.99,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Septic Tank Treatment', 'Biological treatment for maintaining septic systems', 18.00, 2,
        'images/septic_treatment.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Garden Shovel', 'Heavy-duty shovel ideal for digging and planting', 25.50, 3, 'images/garden_shovel.jpg',
        20.99,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Pruning Shears', 'Sharp and ergonomic pruning shears for trimming plants', 14.75, 3,
        'images/pruning_shears.jpg',
        NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Clay Pot', 'Classic clay pot for indoor and outdoor plants', 8.99, 4, 'images/clay_pot.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Hanging Planter', 'Stylish hanging planter with rope support', 11.49, 4, 'images/hanging_planter.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
