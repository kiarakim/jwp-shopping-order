INSERT INTO product (name, price, image_url)
VALUES ('치킨', 10000,
        'http://example.com/chicken.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('샐러드', 20000,
        'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url)
VALUES ('피자', 13000,
        'http://example.com/pizza.jpg');

INSERT INTO member (email, password, points)
VALUES ('a@a.com', '1234', 1320);
INSERT INTO member (email, password, points)
VALUES ('b@b.com', '1234', 100);

INSERT INTO cart_item (member_id, product_id, quantity, checked)
VALUES (1, 1, 2, true);
INSERT INTO cart_item (member_id, product_id, quantity, checked)
VALUES (1, 2, 4, true);

INSERT INTO cart_item (member_id, product_id, quantity, checked)
VALUES (2, 3, 5, true);

INSERT INTO orders (member_id, used_points, saving_rate)
VALUES (1, 1000, 10);

INSERT INTO order_item (order_id, product_id, product_name, product_price, product_quantity, product_image_url)
VALUES (1, 1, '치킨', 10000, 1,
        'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')
