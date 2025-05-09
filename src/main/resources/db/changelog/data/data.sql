-- USERS
INSERT INTO Users (name, email, phone, password, role)
VALUES ('Alice Green', 'alice@example.com', '1234567890', '$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u', 'CLIENT'),
       ('Bob Gardener', 'bob@example.com', '0987654321', '$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u', 'CLIENT'),
       ('Admin Joe', 'admin@example.com', '5555555555', '$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u', 'ADMINISTRATOR');

-- CATEGORIES
INSERT INTO Categories (name)
VALUES ('Plants'),
       ('Tools'),
       ('Soil'),
       ('Seeds');

-- PRODUCTS
INSERT INTO Products (name, description, price, category_id, image_url, discount_price, created_at, updated_at)
VALUES ('Rose Bush', 'Beautiful red rose bush for your garden', 15.99, 1, 'images/rose_bush.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Shovel', 'Durable steel shovel with wooden handle', 25.50, 2, 'images/shovel.jpg', 20.99, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('Potting Soil', 'Nutrient-rich soil for pots and gardens', 10.00, 3, 'images/soil.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Sunflower Seeds', 'Pack of 50 organic sunflower seeds', 3.49, 4, 'images/sunflower_seeds.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Garden Gloves', 'Waterproof gloves with anti-slip grip', 9.99, 2, 'images/gloves.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- FAVORITES
INSERT INTO Favorites (user_id, product_id)
VALUES (1, 2),
       (2, 1),
       (3, 2);

-- CART
-- INSERT INTO Cart (CartID, UserID)
-- VALUES (1, 1),
--        (2, 2);

-- CART ITEMS
-- INSERT INTO CartItems (CartItemID, CartID, ProductID, Quantity)
-- VALUES (1, 1, 1, 2),
--        (2, 1, 3, 1),
--        (3, 2, 4, 3);



-- ORDERS
-- INSERT INTO Orders (OrderID, UserID, CreatedAt, DeliveryAddress, ContactPhone, DeliveryMethod, Status, UpdatedAt)
-- VALUES (1, 1, CURRENT_TIMESTAMP, '123 Green Street', '1234567890', 'Courier', 'Delivered', CURRENT_TIMESTAMP),
--        (2, 2, CURRENT_TIMESTAMP, '456 Flower Road', '0987654321', 'Pickup', 'Processing', CURRENT_TIMESTAMP);

-- ORDER ITEMS
-- INSERT INTO OrderItems (OrderItemID, OrderID, ProductID, Quantity, PriceAtPurchase)
-- VALUES (1, 1, 1, 2, 15.99),
--        (2, 1, 3, 1, 10.00),
--        (3, 2, 4, 3, 3.49);
