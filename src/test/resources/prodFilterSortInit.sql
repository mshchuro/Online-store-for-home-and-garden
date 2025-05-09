INSERT INTO Products (name, description, price, category_id, image_url, discount_price, created_at, updated_at)
VALUES ( 'Rose Bush', 'Beautiful red rose bush for your garden', 15.99, 1, 'images/rose_bush.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ( 'Shovel', 'Durable steel shovel with wooden handle', 25.50, 2, 'images/shovel.jpg', 20.99, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ( 'Potting Soil', 'Nutrient-rich soil for pots and gardens', 10.00, 3, 'images/soil.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ( 'Sunflower Seeds', 'Pack of 50 organic sunflower seeds', 3.49, 4, 'images/sunflower_seeds.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ( 'Garden Gloves', 'Waterproof gloves with anti-slip grip', 9.99, 2, 'images/gloves.jpg', NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);