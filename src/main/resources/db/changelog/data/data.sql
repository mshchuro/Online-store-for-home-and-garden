-- CATEGORIES
INSERT INTO Categories (id, name)
VALUES (1, 'Plants'),
       (2, 'Tools'),
       (3, 'Soil'),
       (4, 'Seeds');

-- PRODUCTS
INSERT INTO Products (id, name, description, price, category_id, image_url, discount_price, created_at, updated_at)
VALUES (1, 'Rose Bush', 'Beautiful red rose bush for your garden', 15.99, 1, 'images/rose_bush.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Shovel', 'Durable steel shovel with wooden handle', 25.50, 2, 'images/shovel.jpg', 20.99, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (3, 'Potting Soil', 'Nutrient-rich soil for pots and gardens', 10.00, 3, 'images/soil.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'Sunflower Seeds', 'Pack of 50 organic sunflower seeds', 3.49, 4, 'images/sunflower_seeds.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 'Garden Gloves', 'Waterproof gloves with anti-slip grip', 9.99, 2, 'images/gloves.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- USERS
INSERT INTO Users (id, name, email, phone, password, user_role)
VALUES (1, 'Alice Green', 'alice@example.com', '1234567890', 'hash1', 'Client'),
       (2, 'Bob Gardener', 'bob@example.com', '0987654321', 'hash2', 'Client'),
       (3, 'Admin Joe', 'admin@example.com', '5555555555', 'adminhash', 'Administrator');

-- CART
-- INSERT INTO Cart (CartID, UserID)
-- VALUES (1, 1),
--        (2, 2);

-- CART ITEMS
-- INSERT INTO CartItems (CartItemID, CartID, ProductID, Quantity)
-- VALUES (1, 1, 1, 2),
--        (2, 1, 3, 1),
--        (3, 2, 4, 3);

-- FAVORITES
-- INSERT INTO Favorites (id, UserID, ProductID)
-- VALUES (1, 1, 1),
--        (2, 1, 5),
--        (3, 2, 2);

-- ORDERS
-- INSERT INTO Orders (OrderID, UserID, CreatedAt, DeliveryAddress, ContactPhone, DeliveryMethod, Status, UpdatedAt)
-- VALUES (1, 1, CURRENT_TIMESTAMP, '123 Green Street', '1234567890', 'Courier', 'Delivered', CURRENT_TIMESTAMP),
--        (2, 2, CURRENT_TIMESTAMP, '456 Flower Road', '0987654321', 'Pickup', 'Processing', CURRENT_TIMESTAMP);

-- ORDER ITEMS
-- INSERT INTO OrderItems (OrderItemID, OrderID, ProductID, Quantity, PriceAtPurchase)
-- VALUES (1, 1, 1, 2, 15.99),
--        (2, 1, 3, 1, 10.00),
--        (3, 2, 4, 3, 3.49);
